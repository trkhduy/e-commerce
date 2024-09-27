package com.dev.identity.service.impl;

import com.dev.commons.Message;
import com.dev.commons.exception.CustomException;
import com.dev.commons.response.ErrorModel;
import com.dev.constant.Constants;
import com.dev.identity.dto.request.UserRequest;
import com.dev.identity.dto.response.UserResponse;
import com.dev.identity.entity.Role;
import com.dev.identity.entity.User;
import com.dev.identity.mapper.UserMapper;
import com.dev.identity.repository.RoleRepository;
import com.dev.identity.repository.UserRepository;
import com.dev.identity.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Override
    public List<UserResponse> getAllUser() {
        List<User> users = userRepository.findAll();
        log.info("Getting all users ...");
        return userMapper.toUserResponseList(users);
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        var userExist = userRepository.existsUserByUsername(request.getUsername());
        if (userExist) {
            log.error("User already existed");
            throw new CustomException(new ErrorModel(400, Message.User.USER_ALREADY_EXISTED));
        }
        try {
            User user = userMapper.toUser(request);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(Constants.Authentication.USER_ROLE));
            user.setRoles(roles);
            log.info("Creating user ...");
            userRepository.save(user);
            return userMapper.toUserResponse(user);
        } catch (Exception e) {
            log.error("Error creating user", e);
            throw new CustomException(new ErrorModel(400, Message.User.CREATE_FAILED));
        }
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
