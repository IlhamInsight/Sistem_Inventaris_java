package com.bangone.erm.models.assets;

import com.bangone.erm.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "asset_conditions") @Data @EqualsAndHashCode(callSuper = true)
public class AssetCondition extends BaseEntity {
    private String name; // Baik, Rusak
}