package com.bangone.erm.controllers.inventory;

import com.bangone.erm.models.inventory.Category;
import com.bangone.erm.services.inventory.CategoryService;
import com.bangone.erm.services.inventory.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventory/categories")
public class CategoryWebController {

    @Autowired private CategoryService categoryService;
    @Autowired private ProductService productService;

    // DAFTAR KATEGORI
    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        return "pages/inventory/category-list";
    }

    // FORM TAMBAH
    @GetMapping("/add")
    public String addForm(Model model) {
        // 1. Bikin objek kategori baru
        Category category = new Category();
        
        // 2. Isi kode otomatis di sini (Backend)
        // Contoh pake format CAT- diikuti 4 angka unik
        String autoCode = "CAT-" + (int)(Math.random() * 9000 + 1000); 
        category.setCode(autoCode);
        
        // 3. Kirim objek yang SUDAH ADA ISINYA ke Model
        model.addAttribute("category", category);
        
        return "pages/inventory/category-form";
    }
    // FORM EDIT
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        Category cat = categoryService.findById(id);
        if (cat == null) return "redirect:/inventory/categories";
        model.addAttribute("category", cat);
        return "pages/inventory/category-form";
    }

    // PROSES SIMPAN (CREATE & UPDATE)
    @PostMapping("/save")
    public String save(@ModelAttribute("category") Category category) {
        categoryService.save(category);
        return "redirect:/inventory/categories";
    }

    // PROSES HAPUS (DENGAN VALIDASI BARANG)
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        // Cek apakah ada barang yang pakai kategori ini
        if (productService.existsByCategoryId(id)) {
            ra.addFlashAttribute("errorMessage", "Gagal hapus! Masih ada produk dengan kategori ini.");
        } else {
            categoryService.delete(id);
            ra.addFlashAttribute("successMessage", "Kategori berhasil dihapus.");
        }
        return "redirect:/inventory/categories";
    }
}