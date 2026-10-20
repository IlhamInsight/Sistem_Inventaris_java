package com.bangone.erm.controllers.auth;

import com.bangone.erm.models.auth.Permission;
import com.bangone.erm.services.auth.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/permissions")
public class PermissionController {
    @Autowired private PermissionService service;
    @GetMapping public List<Permission> getAll() { return service.getAll(); }
    @PostMapping public Permission save(@RequestBody Permission d) { return service.save(d); }
}