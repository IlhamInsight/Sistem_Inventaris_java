package com.bangone.erm.services.transaction;

import com.bangone.erm.models.auth.User;
import com.bangone.erm.models.inventory.Product;
import com.bangone.erm.models.transaction.ApprovalLog;
import com.bangone.erm.models.transaction.LoanDetail;
import com.bangone.erm.models.transaction.LoanRequest;
import com.bangone.erm.repositories.auth.UserRepository;
import com.bangone.erm.repositories.inventory.ProductRepository;
import com.bangone.erm.repositories.transaction.ApprovalLogRepository;
import com.bangone.erm.repositories.transaction.LoanDetailRepository;
import com.bangone.erm.repositories.transaction.LoanRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApprovalLogService {

    private final ApprovalLogRepository approvalLogRepository;
    private final LoanRequestRepository loanRequestRepository;
    private final LoanDetailRepository loanDetailRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ApprovalLogService(ApprovalLogRepository approvalLogRepository,
                              LoanRequestRepository loanRequestRepository,
                              LoanDetailRepository loanDetailRepository,
                              ProductRepository productRepository,
                              UserRepository userRepository) {
        this.approvalLogRepository = approvalLogRepository;
        this.loanRequestRepository = loanRequestRepository;
        this.loanDetailRepository = loanDetailRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<ApprovalLog> getAll() {
        return approvalLogRepository.findAll();
    }

    public ApprovalLog save(ApprovalLog data) {
        return approvalLogRepository.save(data);
    }

    public List<LoanRequest> getPendingLoanRequests() {
        return loanRequestRepository.findByStatus("PENDING");
    }

    @Transactional
    public void approveLoan(String loanRequestId, String username, String comments) {
        LoanRequest loan = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new RuntimeException("Data peminjaman tidak ditemukan."));

        if (!"PENDING".equalsIgnoreCase(loan.getStatus())) {
            throw new RuntimeException("Hanya peminjaman berstatus PENDING yang dapat disetujui.");
        }

        User approver = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User approver tidak ditemukan."));

        // Deduct product stock
        List<LoanDetail> details = loanDetailRepository.findByLoanRequestId(loanRequestId);
        for (LoanDetail detail : details) {
            Product product = detail.getProduct();
            if (product != null) {
                int requestedQty = detail.getQty() != null ? detail.getQty() : 1;
                int currentStock = product.getStock() != null ? product.getStock() : 0;
                if (currentStock < requestedQty) {
                    throw new RuntimeException("Stok tidak mencukupi untuk produk: " + product.getName() 
                            + " (Tersedia: " + currentStock + ", Diminta: " + requestedQty + ")");
                }
                product.setStock(currentStock - requestedQty);
                productRepository.save(product);
            }
        }

        loan.setStatus("APPROVED");
        loanRequestRepository.save(loan);

        ApprovalLog log = new ApprovalLog();
        log.setLoanRequest(loan);
        log.setApprover(approver);
        log.setApprovalStatus("APPROVED");
        log.setComments(comments != null && !comments.isBlank() ? comments : "Disetujui oleh " + approver.getUsername());
        approvalLogRepository.save(log);
    }

    @Transactional
    public void rejectLoan(String loanRequestId, String username, String comments) {
        LoanRequest loan = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new RuntimeException("Data peminjaman tidak ditemukan."));

        if (!"PENDING".equalsIgnoreCase(loan.getStatus())) {
            throw new RuntimeException("Hanya peminjaman berstatus PENDING yang dapat ditolak.");
        }

        User approver = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User approver tidak ditemukan."));

        loan.setStatus("REJECTED");
        loanRequestRepository.save(loan);

        ApprovalLog log = new ApprovalLog();
        log.setLoanRequest(loan);
        log.setApprover(approver);
        log.setApprovalStatus("REJECTED");
        log.setComments(comments != null && !comments.isBlank() ? comments : "Ditolak oleh " + approver.getUsername());
        approvalLogRepository.save(log);
    }
}