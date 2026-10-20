package com.bangone.erm.models.inventory;

import com.bangone.erm.models.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "units")
@Data
@EqualsAndHashCode(callSuper = true)
public class Unit extends BaseEntity {
    private String name; // Pcs, Box, Rim
    private String code;
}