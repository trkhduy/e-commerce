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
public class OrderItem extends BaseEntity {

    Integer quantity;
    Integer productId;
    Integer couponId;
    @ManyToOne
    Order order;
    @ManyToOne
    Shop shop;
}
