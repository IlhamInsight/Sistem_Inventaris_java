package com.bangone.erm.services.auth;
import com.bangone.erm.models.auth.Department;
import com.bangone.erm.repositories.auth.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class DepartmentService {
    @Autowired private DepartmentRepository repository;
    public List<Department> getAll() { return repository.findAll(); }
    public Department getById(String id) { return repository.findById(id).orElse(null); }
    public Department save(Department d) { return repository.save(d); }
    public void delete(String id) { repository.deleteById(id); }
}