package com.dev.identity.mapper;

import com.dev.identity.dto.request.UserRequest;
import com.dev.identity.dto.response.UserResponse;
import com.dev.identity.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    List<UserResponse> toUserResponseList(List<User> users);

    User toUser(UserRequest request);

    //    @Mapping(target = "password", ignore = true)
    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserRequest request);
}
