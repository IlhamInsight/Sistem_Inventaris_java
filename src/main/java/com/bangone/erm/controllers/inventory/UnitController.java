package com.bangone.erm.controllers.inventory;

import com.bangone.erm.models.inventory.Unit;
import com.bangone.erm.services.inventory.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
public class UnitController {

    @Autowired
    private UnitService service;

    @GetMapping
    public List<Unit> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Unit save(@RequestBody Unit unit) {
        return service.save(unit);
    }
}