package com.dev.identity.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Role extends BaseEntity {
    @Column(unique = true)
    String name;
    String description;
    @ManyToMany
    Set<Permission> permissions;
}
