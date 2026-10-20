package com.bangone.erm.controllers.transaction;

import com.bangone.erm.dto.LoanItemForm;
import com.bangone.erm.dto.LoanRequestForm;
import com.bangone.erm.models.auth.User;
import com.bangone.erm.repositories.auth.UserRepository;
import com.bangone.erm.services.auth.EmployeeService;
import com.bangone.erm.services.inventory.ProductService;
import com.bangone.erm.services.transaction.LoanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/loans/requests")
public class LoanController {

    private final LoanService loanService;
    private final EmployeeService employeeService;
    private final ProductService productService;
    private final UserRepository userRepository;

    public LoanController(LoanService loanService,
                          EmployeeService employeeService,
                          ProductService productService,
                          UserRepository userRepository) {
        this.loanService = loanService;
        this.employeeService = employeeService;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String list(Model model, Principal principal) {
        model.addAttribute("loans", loanService.getAll(principal.getName()));
        addRoleAttributes(model, principal);
        return "pages/transaction/loan-list";
    }

    @GetMapping("/add")
    public String addForm(Model model, Principal principal) {
        LoanRequestForm form = new LoanRequestForm();
        form.getItems().add(new LoanItemForm());

        model.addAttribute("loanForm", form);
        addRoleAttributes(model, principal);

        if (isAdminOrStaff(principal)) {
            model.addAttribute("employees", employeeService.getAll());
        }

        if (isKaryawan(principal)) {
            User user = userRepository.findByUsername(principal.getName()).orElse(null);
            model.addAttribute("currentEmployee", user != null ? user.getEmployee() : null);
        }

        model.addAttribute("products", productService.getAll());
        return "pages/transaction/loan-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id,
                           Model model,
                           RedirectAttributes ra,
                           Principal principal) {
        try {
            LoanRequestForm form = loanService.getFormByIdForEdit(id, principal.getName());

            model.addAttribute("loanForm", form);
            addRoleAttributes(model, principal);
            model.addAttribute("employees", employeeService.getAll());
            model.addAttribute("products", productService.getAll());

            return "pages/transaction/loan-form";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/loans/requests";
        }
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("loanForm") LoanRequestForm form,
                       RedirectAttributes ra,
                       Model model,
                       Principal principal) {
        try {
            loanService.save(form, principal.getName());

            if (form.getId() != null && !form.getId().isBlank()) {
                ra.addFlashAttribute("successMessage", "Data peminjaman berhasil diperbarui.");
            } else {
                ra.addFlashAttribute("successMessage", "Data peminjaman berhasil disimpan.");
            }

            return "redirect:/loans/requests";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            addRoleAttributes(model, principal);

            if (isAdminOrStaff(principal)) {
                model.addAttribute("employees", employeeService.getAll());
            }

            if (isKaryawan(principal)) {
                User user = userRepository.findByUsername(principal.getName()).orElse(null);
                model.addAttribute("currentEmployee", user != null ? user.getEmployee() : null);
            }

            model.addAttribute("products", productService.getAll());
            return "pages/transaction/loan-form";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra, Principal principal) {
        try {
            loanService.delete(id, principal.getName());
            ra.addFlashAttribute("successMessage", "Data peminjaman berhasil dihapus.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/loans/requests";
    }

    private void addRoleAttributes(Model model, Principal principal) {
        model.addAttribute("isAdminOrStaff", isAdminOrStaff(principal));
        model.addAttribute("isKaryawan", isKaryawan(principal));
    }

    private boolean isAdminOrStaff(Principal principal) {
        if (principal == null) {
            return false;
        }

        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null || user.getRole() == null || user.getRole().getName() == null) {
            return false;
        }

        String roleName = user.getRole().getName().toUpperCase();
        return roleName.equals("ADMIN") || roleName.equals("STAFF_GUDANG");
    }

    private boolean isKaryawan(Principal principal) {
        if (principal == null) {
            return false;
        }

        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null || user.getRole() == null || user.getRole().getName() == null) {
            return false;
        }

        return user.getRole().getName().equalsIgnoreCase("KARYAWAN");
    }
}