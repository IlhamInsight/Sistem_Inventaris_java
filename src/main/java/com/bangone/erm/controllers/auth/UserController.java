package com.bangone.erm.controllers.auth;

import com.bangone.erm.models.auth.User;
import com.bangone.erm.services.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/users")
public class UserController {
    @Autowired private UserService service;
    @GetMapping public List<User> getAll() { return service.getAll(); }
    @PostMapping public User save(@RequestBody User d) { return service.save(d); }
}