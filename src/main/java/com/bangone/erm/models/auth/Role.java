package com.bangone.erm.models.auth;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @Column(length = 30, nullable = false)
    private String id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;
}