package com.bangone.erm.models.inventory;

import com.bangone.erm.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {
    private String code;
    private String name;
    private String description;
}