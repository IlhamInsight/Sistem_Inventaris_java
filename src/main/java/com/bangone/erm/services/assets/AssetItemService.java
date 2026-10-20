package com.bangone.erm.services.assets;

import com.bangone.erm.models.assets.AssetItem;
import com.bangone.erm.repositories.assets.AssetItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class AssetItemService {
    @Autowired private AssetItemRepository repository;
    public List<AssetItem> getAll() { return repository.findAll(); }
    public AssetItem getById(String id) { return repository.findById(id).orElse(null); }
    public AssetItem save(AssetItem data) { return repository.save(data); }
    public void delete(String id) { repository.deleteById(id); }
    public AssetItem updateStatus(String id, String status) {
        AssetItem item = repository.findById(id).orElseThrow();
        item.setStatus(status);
        return repository.save(item);
    }
}