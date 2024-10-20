package com.dev.identity.service;

import com.dev.identity.dto.request.UserCreationRequest;
import com.dev.identity.dto.request.UserUpdateRequest;
import com.dev.identity.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUser();

    UserResponse createUser(UserCreationRequest request);

    UserResponse updateUser(UserUpdateRequest request, String id);

    UserResponse getUserById(String id);

    UserResponse getMyInfo();

    void deleteUser(String id);
}
