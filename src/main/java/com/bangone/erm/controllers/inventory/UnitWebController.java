package com.bangone.erm.controllers.inventory;

import com.bangone.erm.models.inventory.Unit;
import com.bangone.erm.services.inventory.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventory/units")
public class UnitWebController {

    @Autowired
    private UnitService unitService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("units", unitService.getAll());
        return "pages/inventory/unit-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("unit", new Unit());
        return "pages/inventory/unit-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("unit", unitService.findById(id));
        return "pages/inventory/unit-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Unit unit, RedirectAttributes ra) {
        try {
            unitService.save(unit);
            ra.addFlashAttribute("success", "Unit berhasil disimpan.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Terjadi kesalahan saat menyimpan unit.");
        }
        return "redirect:/inventory/units";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        try {
            unitService.delete(id);
            ra.addFlashAttribute("success", "Unit berhasil dihapus.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Terjadi kesalahan saat menghapus unit.");
        }
        return "redirect:/inventory/units";
    }
}