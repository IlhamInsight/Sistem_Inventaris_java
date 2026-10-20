package com.bangone.erm.controllers.auth;

import com.bangone.erm.models.auth.Department;
import com.bangone.erm.services.auth.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/departments")
public class DepartmentController {
    @Autowired private DepartmentService service;
    @GetMapping public List<Department> getAll() { return service.getAll(); }
    @PostMapping public Department save(@RequestBody Department d) { return service.save(d); }
}