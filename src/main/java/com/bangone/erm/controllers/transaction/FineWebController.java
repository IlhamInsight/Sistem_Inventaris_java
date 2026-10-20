package com.bangone.erm.controllers.transaction;

import com.bangone.erm.models.transaction.Fine;
import com.bangone.erm.services.transaction.FineService;
import com.bangone.erm.services.transaction.LoanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/loans/fines")
public class FineWebController {

    private final FineService fineService;
    private final LoanService loanService;

    public FineWebController(FineService fineService, LoanService loanService) {
        this.fineService = fineService;
        this.loanService = loanService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("fines", fineService.getAll());
        return "pages/transaction/fine-list";
    }

    @GetMapping("/add")
    public String addForm(Model model, Principal principal) {
        model.addAttribute("fine", new Fine());
        model.addAttribute("loans", loanService.getAll(principal.getName()));
        return "pages/transaction/fine-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model, Principal principal, RedirectAttributes ra) {
        Fine fine = fineService.getById(id);
        if (fine == null) {
            ra.addFlashAttribute("errorMessage", "Data denda tidak ditemukan.");
            return "redirect:/loans/fines";
        }
        model.addAttribute("fine", fine);
        model.addAttribute("loans", loanService.getAll(principal.getName()));
        return "pages/transaction/fine-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Fine fine, RedirectAttributes ra) {
        try {
            fineService.save(fine);
            ra.addFlashAttribute("successMessage", "Data denda berhasil disimpan.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Gagal menyimpan denda: " + e.getMessage());
        }
        return "redirect:/loans/fines";
    }

    @PostMapping("/pay/{id}")
    public String markPaid(@PathVariable String id, RedirectAttributes ra) {
        try {
            fineService.markPaid(id);
            ra.addFlashAttribute("successMessage", "Status denda berhasil diubah menjadi LUNAS.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Gagal memperbarui status denda: " + e.getMessage());
        }
        return "redirect:/loans/fines";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        try {
            fineService.delete(id);
            ra.addFlashAttribute("successMessage", "Data denda berhasil dihapus.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Gagal menghapus denda.");
        }
        return "redirect:/loans/fines";
    }
}
