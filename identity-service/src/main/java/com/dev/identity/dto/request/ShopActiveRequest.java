package com.dev.identity.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ShopActiveRequest {

    Integer id;
    Boolean isActive;
    Integer roleId;
}
