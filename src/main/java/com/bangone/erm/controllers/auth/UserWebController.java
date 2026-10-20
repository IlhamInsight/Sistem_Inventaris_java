package com.bangone.erm.controllers.auth;

import com.bangone.erm.models.auth.User;
import com.bangone.erm.services.auth.UserService;
import com.bangone.erm.services.auth.EmployeeService;
import com.bangone.erm.services.auth.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth/users")
public class UserWebController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.getAll());
        return "pages/auth/user-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("roles", roleService.getAll());
        return "pages/auth/user-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        return "pages/auth/user-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.save(user);
            redirectAttributes.addFlashAttribute("success", "User berhasil disimpan.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan saat menyimpan user.");
        }
        return "redirect:/auth/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("success", "User berhasil dihapus.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan saat menghapus user.");
        }
        return "redirect:/auth/users";
    }
}