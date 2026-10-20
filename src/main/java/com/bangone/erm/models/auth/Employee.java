package com.bangone.erm.models.auth;

import com.bangone.erm.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends BaseEntity {

    private String name;

    private String jobTitle;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ToString.Exclude
    @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
    private User user;
}