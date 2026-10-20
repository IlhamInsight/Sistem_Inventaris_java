package com.bangone.erm.controllers.assets;

import com.bangone.erm.models.assets.AssetItem;
import com.bangone.erm.services.assets.AssetItemService;
import com.bangone.erm.services.inventory.LocationService;
import com.bangone.erm.services.inventory.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/assets/items")
public class AssetItemWebController {

    private final AssetItemService service;
    private final ProductService productService;
    private final LocationService locationService;

    public AssetItemWebController(AssetItemService service,
                                  ProductService productService,
                                  LocationService locationService) {
        this.service = service;
        this.productService = productService;
        this.locationService = locationService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("assetItems", service.getAll());
        return "pages/assets/asset-item-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        AssetItem item = new AssetItem();
        item.setStatus("AVAILABLE");
        model.addAttribute("assetItem", item);
        model.addAttribute("products", productService.getAll());
        model.addAttribute("locations", locationService.getAll());
        return "pages/assets/asset-item-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model, RedirectAttributes ra) {
        AssetItem item = service.getById(id);
        if (item == null) {
            ra.addFlashAttribute("error", "Data aset tidak ditemukan.");
            return "redirect:/assets/items";
        }
        model.addAttribute("assetItem", item);
        model.addAttribute("products", productService.getAll());
        model.addAttribute("locations", locationService.getAll());
        return "pages/assets/asset-item-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute AssetItem assetItem, RedirectAttributes ra) {
        try {
            service.save(assetItem);
            ra.addFlashAttribute("success", "Data aset berhasil disimpan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menyimpan data aset: " + e.getMessage());
        }
        return "redirect:/assets/items";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("success", "Data aset berhasil dihapus.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menghapus aset.");
        }
        return "redirect:/assets/items";
    }
}
