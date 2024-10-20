package com.dev.identity.mapper;

import com.dev.identity.dto.request.UserAddressRequest;
import com.dev.identity.entity.UserAddress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {

    UserAddress toUserAddress(UserAddressRequest request);
}
