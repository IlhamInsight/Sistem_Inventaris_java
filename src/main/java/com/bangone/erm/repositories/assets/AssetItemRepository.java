package com.bangone.erm.repositories.assets;

import com.bangone.erm.models.assets.AssetItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetItemRepository extends JpaRepository<AssetItem, String> {
}