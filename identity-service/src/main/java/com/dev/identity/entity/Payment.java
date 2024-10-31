package com.dev.identity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment extends BaseEntity {

    Double amount;
    String paymentMethod;
    String status;
    String transactionId;
    Boolean isRefunded;
    Date paymentDate;
    @ManyToOne
    Shop shop;
}
