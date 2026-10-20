package com.bangone.erm.services.transaction;

import com.bangone.erm.models.transaction.Fine;
import com.bangone.erm.repositories.transaction.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FineService {

    @Autowired
    private FineRepository repository;

    public List<Fine> getAll() {
        return repository.findAll();
    }

    public Fine getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Fine save(Fine data) {
        if (data.getIsPaid() == null) {
            data.setIsPaid(false);
        }
        return repository.save(data);
    }

    public void markPaid(String id) {
        Fine fine = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Data denda tidak ditemukan."));
        fine.setIsPaid(true);
        repository.save(fine);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}