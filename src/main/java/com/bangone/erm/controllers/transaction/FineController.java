package com.bangone.erm.controllers.transaction;

import com.bangone.erm.models.transaction.Fine;
import com.bangone.erm.services.transaction.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/fines")
public class FineController {
    @Autowired private FineService service;
    @GetMapping public List<Fine> getAll() { return service.getAll(); }
    @PostMapping public Fine save(@RequestBody Fine d) { return service.save(d); }
}