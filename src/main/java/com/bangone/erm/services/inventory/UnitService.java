package com.bangone.erm.services.inventory;

import com.bangone.erm.models.inventory.Unit;
import com.bangone.erm.repositories.inventory.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitService {

    @Autowired
    private UnitRepository repository;

    public List<Unit> getAll() {
        return repository.findAll();
    }

    public Unit findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unit ID: " + id + " tidak ditemukan."));
    }

    public Unit save(Unit unit) {
        // Auto-generate kode dari nama
        if (unit.getCode() == null || unit.getCode().trim().isEmpty()) {
            unit.setCode(generateCode(unit.getName()));
        }
        return repository.save(unit);
    }

    public void delete(String id) {
        Unit unit = findById(id);
        repository.delete(unit);
    }

    // Ambil huruf kapital dari setiap kata
    // Contoh: "Pieces" → "PCS", "Box Besar" → "BB", "Rim Kertas" → "RK"
    private String generateCode(String name) {
        if (name == null || name.trim().isEmpty()) return "UNIT";

        String[] words = name.trim().toUpperCase().split("\\s+");

        if (words.length == 1) {
            // Satu kata: ambil max 3 huruf pertama konsonan
            // Contoh: "Pieces" → "PCS", "Pcs" → "PCS"
            String word = words[0].replaceAll("[AEIOU]", "");
            if (word.length() >= 3) return word.substring(0, 3);
            if (word.length() > 0) return word;
            return words[0].substring(0, Math.min(3, words[0].length()));
        }

        // Lebih dari satu kata: ambil huruf pertama tiap kata
        // Contoh: "Box Besar" → "BB", "Rim Kertas" → "RK"
        StringBuilder code = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) code.append(word.charAt(0));
        }
        return code.toString();
    }
}