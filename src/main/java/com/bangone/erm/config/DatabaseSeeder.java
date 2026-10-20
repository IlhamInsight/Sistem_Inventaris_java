package com.bangone.erm.config;

import com.bangone.erm.models.inventory.Category;
import com.bangone.erm.models.inventory.Product;
import com.bangone.erm.models.inventory.Supplier;
import com.bangone.erm.repositories.inventory.CategoryRepository;
import com.bangone.erm.repositories.inventory.ProductRepository;
import com.bangone.erm.repositories.inventory.SupplierRepository;
import com.bangone.erm.models.auth.Department;
import com.bangone.erm.models.auth.Employee;
import com.bangone.erm.models.auth.Role;
import com.bangone.erm.models.auth.User;
import com.bangone.erm.repositories.auth.DepartmentRepository;
import com.bangone.erm.repositories.auth.EmployeeRepository;
import com.bangone.erm.repositories.auth.RoleRepository;
import com.bangone.erm.repositories.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            seedInventaris();
        }
        if (userRepository.count() == 0) {
            seedAuth();
        }
    }

    private void seedInventaris() {
        // 1. Seed Category
        Category elektronik = new Category();
        elektronik.setId(generateId());
        elektronik.setName("Elektronik");
        elektronik.setCode("CAT-ELK");
        categoryRepository.save(elektronik);

        // 2. Seed Supplier
        Supplier vendorA = new Supplier();
        vendorA.setId(generateId());
        vendorA.setName("PT Sinar Jaya");
        vendorA.setCode("SUP-001");
        vendorA.setAddress("Jakarta Pusat");
        supplierRepository.save(vendorA);

        // 3. Seed Product
        Product laptop = new Product();
        laptop.setId(generateId());
        laptop.setCategory(elektronik);
        laptop.setCode("PROD-01");
        laptop.setName("Laptop Lenovo Thinkpad");
        laptop.setPrice(10000000.0);
        laptop.setStock(5);
        productRepository.save(laptop);

        System.out.println(">>> Inventaris Seeded Successfully!");
    }

    private void seedAuth() {
        // 1. Roles
        Role roleAdmin = roleRepository.findByName("ADMIN").orElseGet(() -> {
            Role r = new Role();
            r.setId(generateId());
            r.setName("ADMIN");
            return roleRepository.save(r);
        });

        Role roleStaff = roleRepository.findByName("STAFF_GUDANG").orElseGet(() -> {
            Role r = new Role();
            r.setId(generateId());
            r.setName("STAFF_GUDANG");
            return roleRepository.save(r);
        });

        Role roleKaryawan = roleRepository.findByName("KARYAWAN").orElseGet(() -> {
            Role r = new Role();
            r.setId(generateId());
            r.setName("KARYAWAN");
            return roleRepository.save(r);
        });

        // 2. Department & Employee
        Department itDept = new Department();
        itDept.setId(generateId());
        itDept.setName("Information Technology");
        departmentRepository.save(itDept);

        Employee adminEmp = new Employee();
        adminEmp.setId(generateId());
        adminEmp.setName("Administrator");
        adminEmp.setJobTitle("Super Admin");
        adminEmp.setDepartment(itDept);
        employeeRepository.save(adminEmp);

        // 3. User Admin
        User adminUser = new User();
        adminUser.setId(generateId());
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin"));
        adminUser.setRole(roleAdmin);
        adminUser.setEmployee(adminEmp);
        userRepository.save(adminUser);

        // Staff Gudang
        Employee staffEmp = new Employee();
        staffEmp.setId(generateId());
        staffEmp.setName("Staff Gudang");
        staffEmp.setJobTitle("Inventory Officer");
        staffEmp.setDepartment(itDept);
        employeeRepository.save(staffEmp);

        User staffUser = new User();
        staffUser.setId(generateId());
        staffUser.setUsername("staff");
        staffUser.setPassword(passwordEncoder.encode("staff"));
        staffUser.setRole(roleStaff);
        staffUser.setEmployee(staffEmp);
        userRepository.save(staffUser);

        // Karyawan
        Employee userEmp = new Employee();
        userEmp.setId(generateId());
        userEmp.setName("Budi Karyawan");
        userEmp.setJobTitle("Staff Operasional");
        userEmp.setDepartment(itDept);
        employeeRepository.save(userEmp);

        User karyawanUser = new User();
        karyawanUser.setId(generateId());
        karyawanUser.setUsername("user");
        karyawanUser.setPassword(passwordEncoder.encode("user"));
        karyawanUser.setRole(roleKaryawan);
        karyawanUser.setEmployee(userEmp);
        userRepository.save(karyawanUser);

        System.out.println(">>> Default Users & Roles Seeded Successfully!");
    }

    private String generateId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 26).toUpperCase();
    }
}