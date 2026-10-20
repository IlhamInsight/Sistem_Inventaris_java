package com.bangone.erm.services.auth;

import com.bangone.erm.models.auth.User;
import com.bangone.erm.models.auth.Employee;
import com.bangone.erm.repositories.auth.UserRepository;
import com.bangone.erm.repositories.auth.RoleRepository;
import com.bangone.erm.repositories.auth.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAll() {
        return repository.findAll();
    }

    @Transactional
    public User save(User u) {
        User userToSave;

        if (u.getId() != null && !u.getId().isEmpty()) {
            // UPDATE
            userToSave = repository.findById(u.getId())
                    .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
            userToSave.setUsername(u.getUsername());

            if (u.getPassword() != null && !u.getPassword().trim().isEmpty()) {
                if (!u.getPassword().startsWith("$2a$")) {
                    userToSave.setPassword(passwordEncoder.encode(u.getPassword()));
                } else {
                    userToSave.setPassword(u.getPassword());
                }
            }
        } else {
            // CREATE
            userToSave = new User();
            userToSave.setUsername(u.getUsername());

            if (u.getPassword() == null || u.getPassword().trim().isEmpty()) {
                throw new RuntimeException("Password wajib diisi untuk user baru.");
            }
            userToSave.setPassword(passwordEncoder.encode(u.getPassword()));
        }

        // Resolve Employee dari DB
        Employee selectedEmployee = null;
        if (u.getEmployee() != null
                && u.getEmployee().getId() != null
                && !u.getEmployee().getId().isEmpty()) {
            selectedEmployee = employeeRepository.findById(u.getEmployee().getId())
                    .orElse(null);
            userToSave.setEmployee(selectedEmployee);
        } else {
            userToSave.setEmployee(null);
        }

        // Resolve Role: prioritas dari form
        if (u.getRole() != null
                && u.getRole().getId() != null
                && !u.getRole().getId().isEmpty()) {
            roleRepository.findById(u.getRole().getId())
                    .ifPresent(userToSave::setRole);
        }

        // Fallback dari jobTitle employee
        if (userToSave.getRole() == null && selectedEmployee != null
                && selectedEmployee.getJobTitle() != null
                && !selectedEmployee.getJobTitle().isBlank()) {
            roleRepository.findByName(selectedEmployee.getJobTitle().toUpperCase())
                    .ifPresent(userToSave::setRole);
        }

        // Fallback default KARYAWAN
        if (userToSave.getRole() == null) {
            roleRepository.findByName("KARYAWAN")
                    .ifPresent(userToSave::setRole);
        }

        if (userToSave.getRole() == null) {
            throw new RuntimeException("Role tidak ditemukan. Pastikan data role sudah ada di database.");
        }

        // Simpan user
        User savedUser = repository.save(userToSave);

        // Sinkron sisi Employee
        if (selectedEmployee != null) {
            selectedEmployee.setUser(savedUser);
            employeeRepository.save(selectedEmployee);
        }

        return savedUser;
    }

    public User findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User ID: " + id + " gak ketemu!"));
    }

    @Transactional
    public void delete(String id) {
        User user = repository.findById(id).orElse(null);

        if (user != null) {
            Employee employee = user.getEmployee();

            if (employee != null) {
                employee.setUser(null);
                user.setEmployee(null);
                employeeRepository.save(employee);
            }

            repository.delete(user);
        }
    }
}