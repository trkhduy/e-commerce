package com.dev.identity.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    Integer id;
    Integer productId;
    Integer couponId;
    Integer shopId;
    Integer quantity;
}
