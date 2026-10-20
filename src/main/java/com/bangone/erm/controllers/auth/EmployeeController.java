package com.bangone.erm.controllers.auth;

import com.bangone.erm.models.auth.Employee;
import com.bangone.erm.services.auth.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired private EmployeeService service;
    @GetMapping public List<Employee> getAll() { return service.getAll(); }
    @PostMapping public Employee save(@RequestBody Employee d) { return service.save(d); }
}