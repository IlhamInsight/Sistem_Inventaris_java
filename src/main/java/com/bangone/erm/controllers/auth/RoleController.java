package com.bangone.erm.controllers.auth;

import com.bangone.erm.models.auth.Role;
import com.bangone.erm.services.auth.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/roles")
public class RoleController {
    @Autowired private RoleService service;
    @GetMapping public List<Role> getAll() { return service.getAll(); }
    @PostMapping public Role save(@RequestBody Role d) { return service.save(d); }
}