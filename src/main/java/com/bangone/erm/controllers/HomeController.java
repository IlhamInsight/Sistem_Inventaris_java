package com.bangone.erm.controllers;

import com.bangone.erm.models.auth.User;
import com.bangone.erm.services.auth.UserService;
import com.bangone.erm.services.inventory.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping({"/", "/dashboard", "/home"})
    public String index(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User currentUser = userService.getAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
            
            model.addAttribute("currentUser", currentUser);
        }

        model.addAttribute("htmlContent", "Data Inventaris Terbaru");
        model.addAttribute("products", productService.getAll());
        
        return "index"; 
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}