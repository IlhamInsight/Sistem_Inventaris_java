package com.bangone.erm.services.inventory;

import com.bangone.erm.models.inventory.Location;
import com.bangone.erm.repositories.inventory.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository repository;

    public List<Location> getAll() { return repository.findAll(); }
    public Location getById(String id) { return repository.findById(id).orElse(null); }
    public Location save(Location data) { return repository.save(data); }
    public void delete(String id) { repository.deleteById(id); }
}