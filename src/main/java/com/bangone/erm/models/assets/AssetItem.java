package com.bangone.erm.models.assets;

import com.bangone.erm.models.BaseEntity;
import com.bangone.erm.models.inventory.Product;
import com.bangone.erm.models.inventory.Location;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "asset_items") @Data @EqualsAndHashCode(callSuper = true)
public class AssetItem extends BaseEntity {
    private String serialNumber;
    @ManyToOne @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne @JoinColumn(name = "location_id")
    private Location location;
    private String status; // AVAILABLE, BORROWED
}