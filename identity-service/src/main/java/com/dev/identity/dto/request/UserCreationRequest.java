package com.dev.identity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "Username is required")
    String username;
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String password;
    String firstName;
    String lastName;
    LocalDate dob;
}
