package com.bangone.erm.controllers.inventory;

import com.bangone.erm.models.inventory.Product;
import com.bangone.erm.services.inventory.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/products")
public class ProductController {
    @Autowired private ProductService service;
    @GetMapping public List<Product> getAll() { return service.getAll(); }
    @PostMapping public Product save(@RequestBody Product d) { return service.save(d); }
}