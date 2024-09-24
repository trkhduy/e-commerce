package com.dev.identity.service;

import com.dev.identity.dto.request.UserRequest;
import com.dev.identity.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUser();
    UserResponse createUser(UserRequest request);
    UserResponse getUserById(String id);
    void deleteUser(String id);
}
