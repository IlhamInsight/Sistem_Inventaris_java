package com.bangone.erm.controllers.transaction;

import com.bangone.erm.models.auth.User;
import com.bangone.erm.repositories.auth.UserRepository;
import com.bangone.erm.services.transaction.ReturnService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/loans/returns")
public class ReturnController {

    private final ReturnService returnService;
    private final UserRepository userRepository;

    public ReturnController(ReturnService returnService, UserRepository userRepository) {
        this.returnService = returnService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String list(Model model, Principal principal) {
        model.addAttribute("returns", returnService.getAll(principal.getName()));
        model.addAttribute("isAdminOrStaff", isAdminOrStaff(principal));
        return "pages/transaction/return-list";
    }

    @GetMapping("/add")
    public String addForm(Model model, Principal principal) {
        model.addAttribute("loanRequests", returnService.getAllLoanRequests());
        model.addAttribute("isAdminOrStaff", isAdminOrStaff(principal));
        return "pages/transaction/return-form";
    }

    @PostMapping("/save")
    public String save(@RequestParam("loanRequestId") String loanRequestId,
                       @RequestParam("returnDate") LocalDate returnDate,
                       @RequestParam(value = "conditionAfter", required = false) String conditionAfter,
                       @RequestParam(value = "notes", required = false) String notes,
                       RedirectAttributes ra,
                       Principal principal) {
        try {
            returnService.save(loanRequestId, returnDate, conditionAfter, notes, principal.getName());
            ra.addFlashAttribute("successMessage", "Data pengembalian berhasil disimpan.");
            return "redirect:/loans/returns";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/loans/returns/add";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id,
                         RedirectAttributes ra,
                         Principal principal) {
        try {
            returnService.delete(id, principal.getName());
            ra.addFlashAttribute("successMessage", "Data pengembalian berhasil dihapus.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/loans/returns";
    }

    private boolean isAdminOrStaff(Principal principal) {
        if (principal == null) {
            return false;
        }

        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        return user != null
                && user.getRole() != null
                && user.getRole().getName() != null
                && (
                    user.getRole().getName().equalsIgnoreCase("ADMIN")
                    || user.getRole().getName().equalsIgnoreCase("STAFF_GUDANG")
                );
    }
}