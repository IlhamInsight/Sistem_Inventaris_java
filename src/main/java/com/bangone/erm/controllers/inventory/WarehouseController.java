package com.bangone.erm.controllers.inventory;

import com.bangone.erm.models.inventory.Warehouse;
import com.bangone.erm.services.inventory.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventory/warehouses")
public class WarehouseController {

    private final WarehouseService service;

    public WarehouseController(WarehouseService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("warehouses", service.getAll());
        return "pages/inventory/warehouse-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        Warehouse warehouse = new Warehouse();
        warehouse.setCode("Auto Generate");
        model.addAttribute("warehouse", warehouse);
        return "pages/inventory/warehouse-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model, RedirectAttributes ra) {
        Warehouse warehouse = service.getById(id);
        if (warehouse == null) {
            ra.addFlashAttribute("errorMessage", "Data gudang tidak ditemukan.");
            return "redirect:/inventory/warehouses";
        }

        model.addAttribute("warehouse", warehouse);
        return "pages/inventory/warehouse-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("warehouse") Warehouse warehouse, RedirectAttributes ra) {
        service.save(warehouse);
        ra.addFlashAttribute("successMessage", "Data gudang berhasil disimpan.");
        return "redirect:/inventory/warehouses";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        Warehouse warehouse = service.getById(id);
        if (warehouse == null) {
            ra.addFlashAttribute("errorMessage", "Data gudang tidak ditemukan.");
            return "redirect:/inventory/warehouses";
        }

        service.delete(id);
        ra.addFlashAttribute("successMessage", "Data gudang berhasil dihapus.");
        return "redirect:/inventory/warehouses";
    }
}