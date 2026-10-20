package com.bangone.erm.controllers.inventory;

import com.bangone.erm.models.inventory.Supplier;
import com.bangone.erm.services.inventory.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventory/suppliers")
public class SupplierController {

    private final SupplierService service;

    public SupplierController(SupplierService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("suppliers", service.getAll());
        return "pages/inventory/supplier-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        Supplier supplier = new Supplier();
        supplier.setCode("Auto Generate");
        model.addAttribute("supplier", supplier);
        return "pages/inventory/supplier-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model, RedirectAttributes ra) {
        Supplier supplier = service.getById(id);
        if (supplier == null) {
            ra.addFlashAttribute("errorMessage", "Data supplier tidak ditemukan.");
            return "redirect:/inventory/suppliers";
        }

        model.addAttribute("supplier", supplier);
        return "pages/inventory/supplier-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("supplier") Supplier supplier, RedirectAttributes ra) {
        service.save(supplier);
        ra.addFlashAttribute("successMessage", "Data supplier berhasil disimpan.");
        return "redirect:/inventory/suppliers";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        Supplier supplier = service.getById(id);
        if (supplier == null) {
            ra.addFlashAttribute("errorMessage", "Data supplier tidak ditemukan.");
            return "redirect:/inventory/suppliers";
        }

        service.delete(id);
        ra.addFlashAttribute("successMessage", "Data supplier berhasil dihapus.");
        return "redirect:/inventory/suppliers";
    }
}