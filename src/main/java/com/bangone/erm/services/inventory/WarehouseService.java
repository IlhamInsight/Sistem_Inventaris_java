package com.bangone.erm.services.inventory;

import com.bangone.erm.models.inventory.Warehouse;
import com.bangone.erm.repositories.inventory.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {

    private final WarehouseRepository repository;

    public WarehouseService(WarehouseRepository repository) {
        this.repository = repository;
    }

    public List<Warehouse> getAll() {
        return repository.findAll();
    }

    public Warehouse getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Warehouse save(Warehouse data) {
        if (data.getCode() == null || data.getCode().isBlank() || "Auto Generate".equalsIgnoreCase(data.getCode())) {
            data.setCode(generateCode());
        }
        return repository.save(data);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    private String generateCode() {
        long count = repository.count() + 1;
        String code;

        do {
            code = String.format("GDG-%03d", count++);
        } while (repository.existsByCode(code));

        return code;
    }
}