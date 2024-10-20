package com.dev.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopRequest {
    @NotBlank(message = "Shop name must not be empty")
    String shopName;
    @NotBlank(message = "Contact mail is required")
    @Email
    String contactMail;
    String contactPhone;
}
