package com.bangone.erm.services.assets;

import com.bangone.erm.models.assets.AssetCondition;
import com.bangone.erm.repositories.assets.AssetConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class AssetConditionService {
    @Autowired private AssetConditionRepository repository;
    public List<AssetCondition> getAll() { return repository.findAll(); }
    public AssetCondition save(AssetCondition data) { return repository.save(data); }
}