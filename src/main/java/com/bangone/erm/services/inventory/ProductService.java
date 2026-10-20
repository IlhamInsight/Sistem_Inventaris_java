package com.bangone.erm.services.inventory;

import com.bangone.erm.models.inventory.Product;
import com.bangone.erm.repositories.inventory.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Product save(Product data) {
        return repository.save(data);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public long count() {
        return repository.count();
    }

    public boolean existsByCategoryId(String categoryId) {
        return repository.countByCategoryId(categoryId) > 0;
    }
}