package com.bangone.erm.controllers.inventory;

import com.bangone.erm.models.inventory.Location;
import com.bangone.erm.services.inventory.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventory/locations")
public class LocationWebController {

    private final LocationService service;

    public LocationWebController(LocationService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("locations", service.getAll());
        return "pages/inventory/location-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("location", new Location());
        return "pages/inventory/location-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model, RedirectAttributes ra) {
        Location location = service.getById(id);
        if (location == null) {
            ra.addFlashAttribute("error", "Data lokasi tidak ditemukan.");
            return "redirect:/inventory/locations";
        }
        model.addAttribute("location", location);
        return "pages/inventory/location-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Location location, RedirectAttributes ra) {
        try {
            service.save(location);
            ra.addFlashAttribute("success", "Data lokasi berhasil disimpan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menyimpan lokasi: " + e.getMessage());
        }
        return "redirect:/inventory/locations";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("success", "Data lokasi berhasil dihapus.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menghapus lokasi (mungkin sedang digunakan pada item aset).");
        }
        return "redirect:/inventory/locations";
    }
}
