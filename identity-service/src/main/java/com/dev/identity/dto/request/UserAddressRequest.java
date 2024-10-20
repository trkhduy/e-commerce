package com.dev.identity.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAddressRequest {
    String address;
    String city;
    String postalCode;
    String country;
    String state;
}
