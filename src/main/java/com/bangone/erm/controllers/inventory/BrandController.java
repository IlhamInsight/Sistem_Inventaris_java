package com.bangone.erm.controllers.inventory;

import com.bangone.erm.models.inventory.Brand;
import com.bangone.erm.services.inventory.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/brands")
public class BrandController {
    @Autowired private BrandService service;
    @GetMapping public List<Brand> getAll() { return service.getAll(); }
    @PostMapping public Brand save(@RequestBody Brand d) { return service.save(d); }
}