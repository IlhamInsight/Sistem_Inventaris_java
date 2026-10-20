package com.bangone.erm.controllers.assets;

import com.bangone.erm.models.assets.AssetCondition;
import com.bangone.erm.services.assets.AssetConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/asset-conditions")
public class AssetConditionController {
    @Autowired private AssetConditionService service;
    @GetMapping public List<AssetCondition> getAll() { return service.getAll(); }
    @PostMapping public AssetCondition save(@RequestBody AssetCondition d) { return service.save(d); }
}