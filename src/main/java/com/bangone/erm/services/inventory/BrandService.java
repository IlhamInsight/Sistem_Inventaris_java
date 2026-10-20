package com.bangone.erm.services.inventory;

import com.bangone.erm.models.inventory.Brand;
import com.bangone.erm.repositories.inventory.BrandRepository;
import com.bangone.erm.repositories.inventory.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandRepository repository;

    @Autowired
    private ProductRepository productRepository;

    public List<Brand> getAll() {
        return repository.findAll();
    }

    public Brand findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Brand save(Brand b) {
        return repository.save(b);
    }

    public void delete(String id) {
        Brand brand = repository.findById(id).orElse(null);

        if (brand == null) {
            throw new RuntimeException("Brand tidak ditemukan.");
        }

        long jumlahProduk = productRepository.countProductsByBrandId(id);
        if (jumlahProduk > 0) {
            throw new RuntimeException("Brand tidak bisa dihapus karena masih digunakan oleh produk.");
        }

        repository.deleteById(id);
    }

    public boolean isUsed(String id) {
        return productRepository.countProductsByBrandId(id) > 0;
    }
}