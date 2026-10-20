package com.bangone.erm.controllers.transaction;

import com.bangone.erm.services.transaction.ApprovalLogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/loans/approvals")
public class ApprovalWebController {

    private final ApprovalLogService approvalLogService;

    public ApprovalWebController(ApprovalLogService approvalLogService) {
        this.approvalLogService = approvalLogService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pendingLoans", approvalLogService.getPendingLoanRequests());
        model.addAttribute("approvalLogs", approvalLogService.getAll());
        return "pages/transaction/approval-list";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable String id,
                          @RequestParam(value = "comments", required = false) String comments,
                          Principal principal,
                          RedirectAttributes ra) {
        try {
            approvalLogService.approveLoan(id, principal.getName(), comments);
            ra.addFlashAttribute("successMessage", "Peminjaman berhasil disetujui dan stok barang telah dikurangi.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Gagal menyetujui peminjaman: " + e.getMessage());
        }
        return "redirect:/loans/approvals";
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable String id,
                         @RequestParam(value = "comments", required = false) String comments,
                         Principal principal,
                         RedirectAttributes ra) {
        try {
            approvalLogService.rejectLoan(id, principal.getName(), comments);
            ra.addFlashAttribute("successMessage", "Peminjaman berhasil ditolak.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Gagal menolak peminjaman: " + e.getMessage());
        }
        return "redirect:/loans/approvals";
    }
}
