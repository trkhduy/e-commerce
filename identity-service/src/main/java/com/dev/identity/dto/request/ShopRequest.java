package com.dev.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ShopRequest {
    @NotBlank(message = "Shop name must not be empty")
    String shopName;
    @NotBlank(message = "Contact mail is required")
    @Email
    String contactMail;
    String contactPhone;
}
