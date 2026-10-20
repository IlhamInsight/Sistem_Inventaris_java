package com.bangone.erm.controllers.inventory;

import com.bangone.erm.models.inventory.Category;
import com.bangone.erm.services.inventory.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/categories")
public class CategoryController {
    @Autowired private CategoryService service;
    @GetMapping public List<Category> getAll() { return service.getAll(); }
    @PostMapping public Category save(@RequestBody Category d) { return service.save(d); }
}