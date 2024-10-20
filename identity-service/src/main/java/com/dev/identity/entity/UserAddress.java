package com.dev.identity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAddress extends BaseEntity{
    String address;
    String city;
    String postalCode;
    String country;
    String state;
    @ManyToOne
    User user;
}
