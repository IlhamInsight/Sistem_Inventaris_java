package com.bangone.erm.services.transaction;

import com.bangone.erm.models.auth.User;
import com.bangone.erm.models.inventory.Product;
import com.bangone.erm.models.transaction.LoanDetail;
import com.bangone.erm.models.transaction.LoanRequest;
import com.bangone.erm.models.transaction.ReturnTransaction;
import com.bangone.erm.repositories.auth.UserRepository;
import com.bangone.erm.repositories.inventory.ProductRepository;
import com.bangone.erm.repositories.transaction.LoanDetailRepository;
import com.bangone.erm.repositories.transaction.LoanRequestRepository;
import com.bangone.erm.repositories.transaction.ReturnTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReturnService {

    private final ReturnTransactionRepository returnRepository;
    private final LoanRequestRepository loanRepository;
    private final LoanDetailRepository loanDetailRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReturnService(ReturnTransactionRepository returnRepository,
                         LoanRequestRepository loanRepository,
                         LoanDetailRepository loanDetailRepository,
                         ProductRepository productRepository,
                         UserRepository userRepository) {
        this.returnRepository = returnRepository;
        this.loanRepository = loanRepository;
        this.loanDetailRepository = loanDetailRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<ReturnTransaction> getAll(String username) {
        User user = getUser(username);

        if (isAdminOrStaff(user)) {
            return returnRepository.findAll();
        }

        return returnRepository.findByCreatedById(user.getId());
    }

    public List<LoanRequest> getAllLoanRequests() {
        return loanRepository.findAll()
                .stream()
                .filter(l -> l.getStatus() != null && !l.getStatus().equalsIgnoreCase("RETURNED"))
                .toList();
    }

    @Transactional
    public void save(String loanRequestId,
                     LocalDate returnDate,
                     String conditionAfter,
                     String notes,
                     String username) {

        User user = getUser(username);

        LoanRequest loan = loanRepository.findById(loanRequestId)
                .orElseThrow(() -> new RuntimeException("Data peminjaman tidak ditemukan."));

        if (returnRepository.existsByLoanRequestId(loanRequestId)) {
            throw new RuntimeException("Sudah pernah dikembalikan.");
        }

        List<LoanDetail> details = loanDetailRepository.findByLoanRequestId(loanRequestId);

        for (LoanDetail d : details) {
            Product p = d.getProduct();
            if (p != null) {
                int stock = p.getStock() == null ? 0 : p.getStock();
                int qty = d.getQty() == null ? 0 : d.getQty();
                p.setStock(stock + qty);
                productRepository.save(p);
            }
        }

        ReturnTransaction rt = new ReturnTransaction();
        rt.setLoanRequest(loan);
        rt.setCreatedBy(user);
        rt.setReturnDate(returnDate);
        rt.setConditionAfter(conditionAfter);
        rt.setNotes(notes);
        rt.setStatus("RETURNED");

        returnRepository.save(rt);

        loan.setStatus("RETURNED");
        loanRepository.save(loan);
    }

    public void delete(String id, String username) {
        User user = getUser(username);

        if (!isAdminOrStaff(user)) {
            throw new RuntimeException("Tidak punya akses hapus.");
        }

        returnRepository.deleteById(id);
    }

    // ✅ PERBAIKAN: pesan error lebih informatif + null check
    private User getUser(String username) {
        if (username == null || username.isBlank()) {
            throw new RuntimeException("Sesi login tidak valid. Silakan login ulang.");
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(
                        "User '" + username + "' tidak ditemukan. Silakan logout dan login ulang."));
    }

    // ✅ PERBAIKAN: null-safe
    private boolean isAdminOrStaff(User u) {
        if (u == null || u.getRole() == null || u.getRole().getName() == null) {
            return false;
        }
        String r = u.getRole().getName().toUpperCase();
        return r.equals("ADMIN") || r.equals("STAFF_GUDANG");
    }
}