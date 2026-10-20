package com.bangone.erm.services.assets;

import com.bangone.erm.models.assets.MaintenanceLog;
import com.bangone.erm.models.auth.User;
import com.bangone.erm.models.inventory.Product;
import com.bangone.erm.models.inventory.Supplier;
import com.bangone.erm.repositories.assets.MaintenanceLogRepository;
import com.bangone.erm.repositories.auth.UserRepository;
import com.bangone.erm.repositories.inventory.ProductRepository;
import com.bangone.erm.repositories.inventory.SupplierRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceLogService {

    private final MaintenanceLogRepository maintenanceLogRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    public MaintenanceLogService(MaintenanceLogRepository maintenanceLogRepository,
                                 ProductRepository productRepository,
                                 SupplierRepository supplierRepository,
                                 UserRepository userRepository) {
        this.maintenanceLogRepository = maintenanceLogRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }

    public List<MaintenanceLog> getAll() {
        return maintenanceLogRepository.findAll();
    }

    public MaintenanceLog getById(String id) {
        return maintenanceLogRepository.findById(id).orElse(null);
    }

    public MaintenanceLog getByIdForEdit(String id, String username) {
        MaintenanceLog maintenance = maintenanceLogRepository.findById(id).orElse(null);
        if (maintenance == null) {
            return null;
        }

        User currentUser = getCurrentUser(username);

        if (isAdminOrStaff(currentUser)) {
            return maintenance;
        }

        if (isKaryawan(currentUser)) {
            if (!isPendingStatus(maintenance.getStatus())) {
                throw new RuntimeException("Maintenance dengan status IN_PROGRESS atau DONE tidak bisa diedit oleh karyawan.");
            }
            return maintenance;
        }

        throw new RuntimeException("Anda tidak memiliki akses untuk mengedit data maintenance ini.");
    }

    public void save(String id,
                     String productId,
                     String supplierId,
                     LocalDate maintenanceDate,
                     String issueDescription,
                     String actionTaken,
                     Double cost,
                     String status,
                     String username) {

        User currentUser = getCurrentUser(username);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan."));

        if (maintenanceDate == null) {
            throw new RuntimeException("Tanggal maintenance wajib diisi.");
        }

        Supplier supplier = null;
        if (supplierId != null && !supplierId.isBlank()) {
            supplier = supplierRepository.findById(supplierId).orElse(null);
        }

        MaintenanceLog data;
        boolean isEdit = id != null && !id.isBlank();

        if (isEdit) {
            data = maintenanceLogRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Data maintenance tidak ditemukan."));

            if (isKaryawan(currentUser) && !isPendingStatus(data.getStatus())) {
                throw new RuntimeException("Maintenance dengan status IN_PROGRESS atau DONE tidak bisa diedit oleh karyawan.");
            }

            if (data.getCreatedBy() == null) {
                data.setCreatedBy(currentUser);
            }
        } else {
            data = new MaintenanceLog();
            data.setCreatedBy(currentUser);
            data.setStatus("PENDING");
        }

        data.setProduct(product);
        data.setSupplier(supplier);
        data.setMaintenanceDate(maintenanceDate);
        data.setIssueDescription(issueDescription);
        data.setActionTaken(actionTaken);
        data.setCost(cost == null ? 0.0 : cost);

        String normalizedStatus = normalizeStatus(status);

        if (isAdminOrStaff(currentUser)) {
            data.setStatus(normalizedStatus);
        } else if (isKaryawan(currentUser)) {
            if (!isEdit) {
                data.setStatus("PENDING");
            } else {
                if (!isPendingStatus(data.getStatus())) {
                    throw new RuntimeException("Karyawan hanya bisa mengubah maintenance yang statusnya masih PENDING.");
                }

                if (!"IN_PROGRESS".equals(normalizedStatus) && !"PENDING".equals(normalizedStatus)) {
                    throw new RuntimeException("Karyawan hanya boleh mengubah status ke IN_PROGRESS.");
                }

                data.setStatus(normalizedStatus);
            }
        }

        maintenanceLogRepository.save(data);
    }

    public void delete(String id, String username) {
        User currentUser = getCurrentUser(username);

        if (!isAdminOrStaff(currentUser)) {
            throw new RuntimeException("Hanya admin atau staff gudang yang dapat menghapus data maintenance.");
        }

        MaintenanceLog maintenance = maintenanceLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Data maintenance tidak ditemukan."));

        maintenanceLogRepository.delete(maintenance);
    }

    private User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User login tidak ditemukan."));
    }

    private boolean isAdminOrStaff(User user) {
        if (user == null || user.getRole() == null || user.getRole().getName() == null) {
            return false;
        }

        String roleName = user.getRole().getName().toUpperCase();
        return roleName.equals("ADMIN") || roleName.equals("STAFF_GUDANG");
    }

    private boolean isKaryawan(User user) {
        if (user == null || user.getRole() == null || user.getRole().getName() == null) {
            return false;
        }

        return user.getRole().getName().equalsIgnoreCase("KARYAWAN");
    }

    private boolean isPendingStatus(String status) {
        return status != null && status.equalsIgnoreCase("PENDING");
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return "PENDING";
        }

        String normalized = status.trim().toUpperCase();

        if (!normalized.equals("PENDING")
                && !normalized.equals("IN_PROGRESS")
                && !normalized.equals("DONE")) {
            throw new RuntimeException("Status maintenance tidak valid.");
        }

        return normalized;
    }
}