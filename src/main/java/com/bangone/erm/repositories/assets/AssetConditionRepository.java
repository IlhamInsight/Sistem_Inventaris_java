package com.bangone.erm.repositories.assets;

import com.bangone.erm.models.assets.AssetCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetConditionRepository extends JpaRepository<AssetCondition, String> {
}