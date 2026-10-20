package com.bangone.erm.controllers.assets;

import com.bangone.erm.models.assets.AssetItem;
import com.bangone.erm.services.assets.AssetItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/asset-items")
public class AssetItemController {
    @Autowired private AssetItemService service;
    @GetMapping public List<AssetItem> getAll() { return service.getAll(); }
    @PostMapping public AssetItem save(@RequestBody AssetItem d) { return service.save(d); }
}