package com.dev.identity.service.impl;

import com.dev.commons.Message;
import com.dev.commons.exception.CustomException;
import com.dev.commons.response.ErrorModel;
import com.dev.constant.Constants;
import com.dev.identity.dto.request.UserCreationRequest;
import com.dev.identity.dto.request.UserUpdateRequest;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public UserResponse createUser(UserCreationRequest request) {
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
            log.error("Error creating user!", e);
            throw new CustomException(new ErrorModel(400, Message.User.CREATE_FAILED));
        }
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest request, String id) {
        User user = findById(id);
        try {
            userMapper.updateUser(user, request);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            var roles = roleRepository.findAllById(request.getRoles());
            user.setRoles(new HashSet<>(roles));
            log.info("Updating user ...");
            return userMapper.toUserResponse(userRepository.save(user));
        } catch (Exception e) {
            log.error("Error updating user", e);
            throw new CustomException(new ErrorModel(400, Message.User.UPDATE_FAILED));
        }
    }

    @Override
    public UserResponse getUserById(String id) {
        User user = findById(id);
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getMyInfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        log.info("Get info for user {}", username);
        User userInfo = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(new ErrorModel(400, Message.User.USER_DOES_NOT_EXITED)));
        return userMapper.toUserResponse(userInfo);
    }

    @Override
    public void deleteUser(String id) {
        log.info("Deleting user ...");
        userRepository.deleteById(id);
    }

    private User findById(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new CustomException(new ErrorModel(400, Message.User.USER_DOES_NOT_EXITED)));
    }
}
