package com.bangone.erm.controllers.assets;

import com.bangone.erm.models.assets.MaintenanceLog;
import com.bangone.erm.models.auth.User;
import com.bangone.erm.repositories.auth.UserRepository;
import com.bangone.erm.services.assets.MaintenanceLogService;
import com.bangone.erm.services.inventory.ProductService;
import com.bangone.erm.services.inventory.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/maintenance/logs")
public class MaintenanceLogController {

    private final MaintenanceLogService maintenanceLogService;
    private final ProductService productService;
    private final SupplierService supplierService;
    private final UserRepository userRepository;

    public MaintenanceLogController(MaintenanceLogService maintenanceLogService,
                                    ProductService productService,
                                    SupplierService supplierService,
                                    UserRepository userRepository) {
        this.maintenanceLogService = maintenanceLogService;
        this.productService = productService;
        this.supplierService = supplierService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String list(Model model, Principal principal) {
        model.addAttribute("maintenances", maintenanceLogService.getAll());
        addRoleAttributes(model, principal);
        return "pages/assets/maintenance-list";
    }

    @GetMapping("/add")
    public String addForm(Model model, Principal principal) {
        model.addAttribute("maintenance", new MaintenanceLog());
        model.addAttribute("products", productService.getAll());
        model.addAttribute("suppliers", supplierService.getAll());
        addRoleAttributes(model, principal);
        return "pages/assets/maintenance-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id,
                           Model model,
                           RedirectAttributes ra,
                           Principal principal) {
        try {
            MaintenanceLog maintenance = maintenanceLogService.getByIdForEdit(id, principal.getName());

            if (maintenance == null) {
                ra.addFlashAttribute("errorMessage", "Data maintenance tidak ditemukan.");
                return "redirect:/maintenance/logs";
            }

            model.addAttribute("maintenance", maintenance);
            model.addAttribute("products", productService.getAll());
            model.addAttribute("suppliers", supplierService.getAll());
            addRoleAttributes(model, principal);
            return "pages/assets/maintenance-form";

        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/maintenance/logs";
        }
    }

    @PostMapping("/save")
    public String save(@RequestParam(value = "id", required = false) String id,
                       @RequestParam("productId") String productId,
                       @RequestParam(value = "supplierId", required = false) String supplierId,
                       @RequestParam("maintenanceDate") java.time.LocalDate maintenanceDate,
                       @RequestParam(value = "issueDescription", required = false) String issueDescription,
                       @RequestParam(value = "actionTaken", required = false) String actionTaken,
                       @RequestParam(value = "cost", required = false) Double cost,
                       @RequestParam(value = "status", required = false) String status,
                       RedirectAttributes ra,
                       Model model,
                       Principal principal) {
        try {
            maintenanceLogService.save(
                    id,
                    productId,
                    supplierId,
                    maintenanceDate,
                    issueDescription,
                    actionTaken,
                    cost,
                    status,
                    principal.getName()
            );

            ra.addFlashAttribute("successMessage", "Data maintenance berhasil disimpan.");
            return "redirect:/maintenance/logs";

        } catch (Exception e) {
            MaintenanceLog maintenance = new MaintenanceLog();
            maintenance.setId(id);

            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("maintenance", maintenance);
            model.addAttribute("products", productService.getAll());
            model.addAttribute("suppliers", supplierService.getAll());
            addRoleAttributes(model, principal);
            return "pages/assets/maintenance-form";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id,
                         RedirectAttributes ra,
                         Principal principal) {
        try {
            maintenanceLogService.delete(id, principal.getName());
            ra.addFlashAttribute("successMessage", "Data maintenance berhasil dihapus.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/maintenance/logs";
    }

    private void addRoleAttributes(Model model, Principal principal) {
        boolean isAdminOrStaff = false;
        boolean isKaryawan = false;

        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName()).orElse(null);

            if (user != null && user.getRole() != null && user.getRole().getName() != null) {
                String roleName = user.getRole().getName().toUpperCase();
                isAdminOrStaff = roleName.equals("ADMIN") || roleName.equals("STAFF_GUDANG");
                isKaryawan = roleName.equals("KARYAWAN");
            }
        }

        model.addAttribute("isAdminOrStaff", isAdminOrStaff);
        model.addAttribute("isKaryawan", isKaryawan);
    }
}