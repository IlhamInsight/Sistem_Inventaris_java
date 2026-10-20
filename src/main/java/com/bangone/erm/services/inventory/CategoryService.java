package com.bangone.erm.services.inventory;

import com.bangone.erm.models.inventory.Category;
import com.bangone.erm.repositories.inventory.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    @Autowired private CategoryRepository repository;

    public List<Category> getAll() { return repository.findAll(); }
    
    public Category save(Category c) { return repository.save(c); }
    
    public Category findById(String id) { 
        return repository.findById(id).orElse(null); 
    }
    
    public void delete(String id) { repository.deleteById(id); }
}