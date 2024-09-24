package com.dev.identity.service.impl;

import com.dev.identity.dto.request.UserRequest;
import com.dev.identity.dto.response.UserResponse;
import com.dev.identity.entity.User;
import com.dev.identity.mapper.UserMapper;
import com.dev.identity.repository.UserRepository;
import com.dev.identity.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public List<UserResponse> getAllUser() {
        List<User> users = userRepository.findAll();
        log.info("Getting all users ...");
        return userMapper.toUserResponseList(users);
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        User user = userMapper.toUser(request);
        log.info("Creating user ...");
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
