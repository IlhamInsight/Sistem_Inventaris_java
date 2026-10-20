package com.bangone.erm.controllers.inventory;

import com.bangone.erm.models.inventory.Location;
import com.bangone.erm.services.inventory.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/locations")
public class LocationController {
    @Autowired private LocationService service;
    @GetMapping public List<Location> getAll() { return service.getAll(); }
    @PostMapping public Location save(@RequestBody Location d) { return service.save(d); }
}