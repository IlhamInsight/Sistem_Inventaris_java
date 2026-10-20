package com.bangone.erm.controllers.inventory;

import com.bangone.erm.models.inventory.Brand;
import com.bangone.erm.services.inventory.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/inventory/brands")
public class BrandWebController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public String list(Model model) {
        List<Brand> brands = brandService.getAll();

        Map<String, Boolean> brandUsedMap = new HashMap<>();
        for (Brand brand : brands) {
            brandUsedMap.put(brand.getId(), brandService.isUsed(brand.getId()));
        }

        model.addAttribute("brands", brands);
        model.addAttribute("brandUsedMap", brandUsedMap);

        return "pages/inventory/brand-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("brand", new Brand());
        return "pages/inventory/brand-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        Brand brand = brandService.findById(id);
        if (brand != null) {
            model.addAttribute("brand", brand);
            return "pages/inventory/brand-form";
        }
        return "redirect:/inventory/brands";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("brand") Brand brand,
                       RedirectAttributes redirectAttributes) {
        try {
            brandService.save(brand);
            redirectAttributes.addFlashAttribute("success", "Brand berhasil disimpan.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan saat menyimpan brand.");
        }
        return "redirect:/inventory/brands";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id,
                         RedirectAttributes redirectAttributes) {
        try {
            brandService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Brand berhasil dihapus.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan saat menghapus brand.");
        }
        return "redirect:/inventory/brands";
    }
}