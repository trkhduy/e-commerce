package com.dev.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username no more than 50 characters")
    String username;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String password;
    @Size(max = 50, message = "First name no more than 50 characters")
    String firstName;
    @Size(max = 50, message = "Last name no more than 50 characters")
    String lastName;
    @NotBlank(message = "Email is required")
    @Email
    String email;
    String phoneNumber;
    ShopRequest shop;
}
