package com.bangone.erm.controllers.inventory;

import com.bangone.erm.models.inventory.Product;
import com.bangone.erm.services.inventory.ProductService;
import com.bangone.erm.services.inventory.CategoryService;
import com.bangone.erm.services.inventory.BrandService; // Tambahkan import ini
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventory/products")
public class ProductWebController {

    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;
    @Autowired private BrandService brandService; // Tambahkan autowired ini

    // 1. Menampilkan Daftar Produk
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAll());
        return "pages/inventory/product-list"; 
    }

    // 2. Menampilkan Form Tambah
    @GetMapping("/add")
    public String addForm(Model model) {
        Product product = new Product();
        
        // Generate kode otomatis: Hitung jumlah data + 1
        // Contoh format: PROD-0001
        long nextId = productService.count() + 1;
        String autoCode = String.format("PROD-%04d", nextId);
        
        product.setCode(autoCode); // Set kode ke objek produk
        
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("brands", brandService.getAll()); // Kirim data Brand ke form
        return "pages/inventory/product-form";
    }

    // 3. Menampilkan Form Edit
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        // Ambil data produk lama berdasarkan ID untuk diedit
        model.addAttribute("product", productService.getById(id)); 
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("brands", brandService.getAll()); // Kirim data Brand ke form
        return "pages/inventory/product-form";
    }

    // 4. Proses Simpan (Tambah & Update)
    @PostMapping("/save")
    public String save(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/inventory/products";
    }

    // 5. Proses Hapus
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        productService.delete(id); 
        return "redirect:/inventory/products";
    }
}