package com.dev.identity.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    Integer userId;
    Double totalAmount;
    List<OrderItemRequest> orderItems;
}
