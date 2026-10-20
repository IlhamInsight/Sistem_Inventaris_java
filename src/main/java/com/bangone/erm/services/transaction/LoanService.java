package com.bangone.erm.services.transaction;

import com.bangone.erm.dto.LoanItemForm;
import com.bangone.erm.dto.LoanRequestForm;
import com.bangone.erm.models.auth.Employee;
import com.bangone.erm.models.auth.User;
import com.bangone.erm.models.inventory.Product;
import com.bangone.erm.models.transaction.LoanDetail;
import com.bangone.erm.models.transaction.LoanRequest;
import com.bangone.erm.repositories.auth.EmployeeRepository;
import com.bangone.erm.repositories.auth.UserRepository;
import com.bangone.erm.repositories.inventory.ProductRepository;
import com.bangone.erm.repositories.transaction.LoanDetailRepository;
import com.bangone.erm.repositories.transaction.LoanRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    private final LoanRequestRepository loanRequestRepository;
    private final LoanDetailRepository loanDetailRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public LoanService(LoanRequestRepository loanRequestRepository,
                       LoanDetailRepository loanDetailRepository,
                       EmployeeRepository employeeRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.loanDetailRepository = loanDetailRepository;
        this.employeeRepository = employeeRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<LoanRequest> getAll(String username) {
        User currentUser = getCurrentUser(username);

        if (isAdminOrStaff(currentUser)) {
            return loanRequestRepository.findAll();
        }

        if (isKaryawan(currentUser)) {
            if (currentUser.getEmployee() == null) {
                throw new RuntimeException("User karyawan tidak terhubung ke data employee.");
            }
            return loanRequestRepository.findByEmployee_Id(currentUser.getEmployee().getId());
        }

        return List.of();
    }

    public LoanRequest getById(String id) {
        return loanRequestRepository.findById(id).orElse(null);
    }

    public LoanRequestForm getFormByIdForEdit(String id, String username) {
        User currentUser = getCurrentUser(username);

        if (!isAdminOrStaff(currentUser)) {
            throw new RuntimeException("Hanya admin atau staff gudang yang dapat mengedit peminjaman.");
        }

        LoanRequest request = loanRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Data peminjaman tidak ditemukan."));

        LoanRequestForm form = new LoanRequestForm();
        form.setId(request.getId());
        form.setEmployeeId(request.getEmployee() != null ? request.getEmployee().getId() : null);
        form.setLoanDate(request.getLoanDate());
        form.setDueDate(request.getDueDate());
        form.setPurpose(request.getPurpose());
        form.setNotes(request.getNotes());
        form.setStatus(request.getStatus());

        List<LoanDetail> details = loanDetailRepository.findByLoanRequestId(request.getId());
        List<LoanItemForm> itemForms = new ArrayList<>();

        for (LoanDetail detail : details) {
            LoanItemForm itemForm = new LoanItemForm();
            itemForm.setProductId(detail.getProduct() != null ? detail.getProduct().getId() : null);
            itemForm.setQty(detail.getQty());
            itemForm.setConditionBefore(detail.getConditionBefore());
            itemForm.setNotes(detail.getNotes());
            itemForms.add(itemForm);
        }

        if (itemForms.isEmpty()) {
            itemForms.add(new LoanItemForm());
        }

        form.setItems(itemForms);
        return form;
    }

    @Transactional
    public void save(LoanRequestForm form, String username) {
        User currentUser = getCurrentUser(username);
        boolean isEdit = form.getId() != null && !form.getId().isBlank();

        if (isEdit) {
            updateLoan(form, currentUser);
        } else {
            createLoan(form, currentUser);
        }
    }

    private void createLoan(LoanRequestForm form, User currentUser) {
        Employee employee;

        if (isAdminOrStaff(currentUser)) {
            employee = employeeRepository.findById(form.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Karyawan tidak ditemukan."));
        } else if (isKaryawan(currentUser)) {
            if (currentUser.getEmployee() == null) {
                throw new RuntimeException("User karyawan tidak terhubung ke data employee.");
            }
            employee = currentUser.getEmployee();
        } else {
            throw new RuntimeException("Anda tidak memiliki akses untuk membuat peminjaman.");
        }

        validateLoanForm(form);

        LoanRequest request = new LoanRequest();
        request.setRequestCode(generateRequestCode());
        request.setEmployee(employee);
        request.setCreatedBy(currentUser);
        request.setRequestDate(LocalDate.now());
        request.setLoanDate(form.getLoanDate());
        request.setDueDate(form.getDueDate());
        request.setPurpose(form.getPurpose());
        request.setStatus("PENDING");
        request.setNotes(form.getNotes());

        loanRequestRepository.save(request);
        saveLoanDetails(request, form.getItems());
    }

    private void updateLoan(LoanRequestForm form, User currentUser) {
        if (!isAdminOrStaff(currentUser)) {
            throw new RuntimeException("Hanya admin atau staff gudang yang dapat mengedit peminjaman.");
        }

        LoanRequest request = loanRequestRepository.findById(form.getId())
                .orElseThrow(() -> new RuntimeException("Data peminjaman tidak ditemukan."));

        Employee employee = employeeRepository.findById(form.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Karyawan tidak ditemukan."));

        validateLoanForm(form);

        request.setEmployee(employee);
        request.setLoanDate(form.getLoanDate());
        request.setDueDate(form.getDueDate());
        request.setPurpose(form.getPurpose());
        request.setNotes(form.getNotes());
        request.setStatus(normalizeStatus(form.getStatus()));

        loanRequestRepository.save(request);

        List<LoanDetail> oldDetails = loanDetailRepository.findByLoanRequestId(request.getId());
        loanDetailRepository.deleteAll(oldDetails);

        saveLoanDetails(request, form.getItems());
    }

    private void validateLoanForm(LoanRequestForm form) {
        if (form.getLoanDate() == null) {
            throw new RuntimeException("Tanggal pinjam wajib diisi.");
        }
        if (form.getDueDate() == null) {
            throw new RuntimeException("Tanggal jatuh tempo wajib diisi.");
        }
        if (form.getDueDate().isBefore(form.getLoanDate())) {
            throw new RuntimeException("Tanggal jatuh tempo tidak boleh sebelum tanggal pinjam.");
        }
        if (form.getItems() == null || form.getItems().isEmpty()) {
            throw new RuntimeException("Minimal harus ada satu barang yang dipinjam.");
        }
    }

    private void saveLoanDetails(LoanRequest request, List<LoanItemForm> items) {
        boolean hasValidItem = false;

        for (LoanItemForm item : items) {
            if (item == null || item.getProductId() == null || item.getProductId().isBlank()) {
                continue;
            }

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan."));

            if (item.getQty() == null || item.getQty() <= 0) {
                throw new RuntimeException("Qty harus lebih dari 0.");
            }

            LoanDetail detail = new LoanDetail();
            detail.setLoanRequest(request);
            detail.setProduct(product);
            detail.setQty(item.getQty());
            detail.setConditionBefore(item.getConditionBefore());
            detail.setNotes(item.getNotes());

            loanDetailRepository.save(detail);
            hasValidItem = true;
        }

        if (!hasValidItem) {
            throw new RuntimeException("Minimal harus ada satu barang yang valid.");
        }
    }

    @Transactional
    public void delete(String id, String username) {
        User currentUser = getCurrentUser(username);

        if (!isAdminOrStaff(currentUser)) {
            throw new RuntimeException("Hanya admin atau staff gudang yang dapat menghapus data peminjaman.");
        }

        LoanRequest request = loanRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Data peminjaman tidak ditemukan."));

        List<LoanDetail> details = loanDetailRepository.findByLoanRequestId(id);
        loanDetailRepository.deleteAll(details);
        loanRequestRepository.delete(request);
    }

    private String generateRequestCode() {
        long count = loanRequestRepository.count() + 1;
        String code;
        do {
            code = String.format("LOAN-%03d", count++);
        } while (loanRequestRepository.existsByRequestCode(code));
        return code;
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) return "PENDING";

        String normalized = status.trim().toUpperCase();
        if (!normalized.equals("PENDING")
                && !normalized.equals("APPROVED")
                && !normalized.equals("REJECTED")
                && !normalized.equals("RETURNED")) {
            throw new RuntimeException("Status peminjaman tidak valid.");
        }
        return normalized;
    }

    // ✅ PERBAIKAN: pesan error lebih informatif
    private User getCurrentUser(String username) {
        if (username == null || username.isBlank()) {
            throw new RuntimeException("Sesi login tidak valid. Silakan login ulang.");
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(
                        "User '" + username + "' tidak ditemukan. Silakan logout dan login ulang."));
    }

    // ✅ PERBAIKAN: null-safe
    private boolean isAdminOrStaff(User user) {
        if (user == null || user.getRole() == null || user.getRole().getName() == null) {
            return false;
        }
        String roleName = user.getRole().getName().toUpperCase();
        return roleName.equals("ADMIN") || roleName.equals("STAFF_GUDANG");
    }

    // ✅ PERBAIKAN: null-safe
    private boolean isKaryawan(User user) {
        if (user == null || user.getRole() == null || user.getRole().getName() == null) {
            return false;
        }
        return user.getRole().getName().equalsIgnoreCase("KARYAWAN");
    }
}