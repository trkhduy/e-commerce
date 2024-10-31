package com.dev.identity.service;

import com.dev.identity.dto.request.ShopActiveRequest;
import com.dev.identity.dto.request.UserCreationRequest;
import com.dev.identity.dto.request.UserUpdateRequest;
import com.dev.identity.dto.response.CountUserByMonth;
import com.dev.identity.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUser();

    UserResponse createUser(UserCreationRequest request);

    UserResponse updateUser(UserUpdateRequest request, Integer id);

    UserResponse getUserById(Integer id);

    UserResponse getMyInfo();

    Boolean updateUserStatus(Integer id);


    Boolean updateStatus(ShopActiveRequest request);

    void deleteUser(Integer id);

    List<CountUserByMonth> countUserRegistryByMonth();
}
