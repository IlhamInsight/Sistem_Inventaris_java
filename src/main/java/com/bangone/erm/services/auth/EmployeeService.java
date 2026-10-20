package com.bangone.erm.services.auth;

import com.bangone.erm.models.auth.Employee;
import com.bangone.erm.repositories.auth.EmployeeRepository;
import com.bangone.erm.repositories.transaction.LoanRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final LoanRequestRepository loanRequestRepository;

    public EmployeeService(EmployeeRepository repository,
                           LoanRequestRepository loanRequestRepository) {
        this.repository = repository;
        this.loanRequestRepository = loanRequestRepository;
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Employee getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Employee save(Employee data) {
        return repository.save(data);
    }

    public void delete(String id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee tidak ditemukan"));

        long totalLoanRequest = loanRequestRepository.countByEmployee_Id(id);
        if (totalLoanRequest > 0) {
            throw new IllegalStateException(
                    "Employee tidak bisa dihapus karena masih digunakan pada "
                            + totalLoanRequest + " data loan request."
            );
        }

        repository.delete(employee);
    }
}