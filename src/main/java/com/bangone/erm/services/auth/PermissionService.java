package com.bangone.erm.services.auth;

import com.bangone.erm.models.auth.Permission;
import com.bangone.erm.repositories.auth.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class PermissionService {
    @Autowired private PermissionRepository repository;
    public List<Permission> getAll() { return repository.findAll(); }
    public Permission save(Permission p) { return repository.save(p); }
}