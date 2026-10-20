package com.bangone.erm.controllers.auth;

import com.bangone.erm.models.auth.Employee;
import com.bangone.erm.services.auth.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth/employees")
public class EmployeeWebController {

    private final EmployeeService employeeService;
    private final com.bangone.erm.services.auth.DepartmentService departmentService;

    public EmployeeWebController(EmployeeService employeeService,
                                 com.bangone.erm.services.auth.DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    // 1. Daftar Karyawan
    @GetMapping
    public String list(Model model) {
        model.addAttribute("employees", employeeService.getAll());
        return "pages/auth/employee-list";
    }

    // 2. Form Tambah
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentService.getAll());
        return "pages/auth/employee-form";
    }

    // 3. Form Edit
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        Employee e = employeeService.getById(id);

        if (e == null) {
            redirectAttributes.addFlashAttribute("error", "Data employee tidak ditemukan.");
            return "redirect:/auth/employees";
        }

        model.addAttribute("employee", e);
        model.addAttribute("departments", departmentService.getAll());
        return "pages/auth/employee-form";
    }

    // 4. Simpan Data
    @PostMapping("/save")
    public String save(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        try {
            employeeService.save(employee);
            redirectAttributes.addFlashAttribute("success", "Data employee berhasil disimpan.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menyimpan data employee.");
        }

        return "redirect:/auth/employees";
    }

    // 5. Hapus Data
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Data employee berhasil dihapus.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan saat menghapus data employee.");
        }

        return "redirect:/auth/employees";
    }
}