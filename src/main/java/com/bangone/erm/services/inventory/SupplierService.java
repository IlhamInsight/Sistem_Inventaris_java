package com.bangone.erm.services.inventory;

import com.bangone.erm.models.inventory.Supplier;
import com.bangone.erm.repositories.inventory.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository repository;

    public SupplierService(SupplierRepository repository) {
        this.repository = repository;
    }

    public List<Supplier> getAll() {
        return repository.findAll();
    }

    public Supplier getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Supplier save(Supplier data) {
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
            code = String.format("SUP-%03d", count++);
        } while (repository.existsByCode(code));

        return code;
    }
}