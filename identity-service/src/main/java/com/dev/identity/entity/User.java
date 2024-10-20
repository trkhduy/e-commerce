package com.dev.identity.entity;

import com.dev.identity.enumeration.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(unique = true)
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    LocalDate dob;
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.INACTIVE;
    @ManyToMany
    Set<Role> roles;
}
