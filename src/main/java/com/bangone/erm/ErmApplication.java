package com.bangone.erm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ErmApplication {

    private static final Logger log = LoggerFactory.getLogger(ErmApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ErmApplication.class, args);
        
        log.info("===============================================");
        log.info("SISTEM INVENTARIS BERHASIL DIJALANKAN!");
        log.info("Akses API Produk: http://localhost:8080/api/products");
        log.info("===============================================");
    }
}