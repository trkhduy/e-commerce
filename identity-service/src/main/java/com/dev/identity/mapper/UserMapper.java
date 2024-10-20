package com.dev.identity.mapper;

import com.dev.identity.dto.request.UserCreationRequest;
import com.dev.identity.dto.request.UserUpdateRequest;
import com.dev.identity.dto.response.UserResponse;
import com.dev.identity.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    List<UserResponse> toUserResponseList(List<User> users);

    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
