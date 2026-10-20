package com.bangone.erm.controllers.auth;

import com.bangone.erm.models.auth.Department;
import com.bangone.erm.services.auth.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth/departments")
public class DepartmentWebController {

    private final DepartmentService service;

    public DepartmentWebController(DepartmentService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("departments", service.getAll());
        return "pages/auth/department-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("department", new Department());
        return "pages/auth/department-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model, RedirectAttributes ra) {
        Department dept = service.getById(id);
        if (dept == null) {
            ra.addFlashAttribute("error", "Departemen tidak ditemukan.");
            return "redirect:/auth/departments";
        }
        model.addAttribute("department", dept);
        return "pages/auth/department-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Department department, RedirectAttributes ra) {
        try {
            service.save(department);
            ra.addFlashAttribute("success", "Departemen berhasil disimpan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menyimpan departemen: " + e.getMessage());
        }
        return "redirect:/auth/departments";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("success", "Departemen berhasil dihapus.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menghapus departemen (mungkin sedang digunakan oleh data karyawan).");
        }
        return "redirect:/auth/departments";
    }
}
