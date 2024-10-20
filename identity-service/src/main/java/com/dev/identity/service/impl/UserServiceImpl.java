package com.dev.identity.service.impl;

import com.dev.commons.Message;
import com.dev.commons.exception.CustomException;
import com.dev.commons.response.ErrorModel;
import com.dev.constant.Constants;
import com.dev.identity.dto.request.UserCreationRequest;
import com.dev.identity.dto.request.UserUpdateRequest;
import com.dev.identity.dto.response.UserResponse;
import com.dev.identity.entity.Role;
import com.dev.identity.entity.Shop;
import com.dev.identity.entity.User;
import com.dev.identity.mapper.ShopMapper;
import com.dev.identity.mapper.UserMapper;
import com.dev.identity.repository.RoleRepository;
import com.dev.identity.repository.ShopRepository;
import com.dev.identity.repository.UserRepository;
import com.dev.identity.service.UserService;
import com.dev.identity.util.SendEmailUtil;
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
    ShopRepository shopRepository;
    ShopMapper shopMapper;
    SendEmailUtil sendEmailUtil;

    @Override
    public List<UserResponse> getAllUser() {
        List<User> users = userRepository.findAll();
        log.info("Getting all users ...");
        return userMapper.toUserResponseList(users);
    }

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        var userExist = userRepository.existsUserByUsername(request.getUsername());
        var emailExist = userRepository.existsUserByEmail(request.getEmail());
        if (userExist) {
            log.error(Message.User.USER_ALREADY_EXISTED);
            throw new CustomException(new ErrorModel(400, Message.User.USER_ALREADY_EXISTED));
        }
//        if (emailExist) {
//            log.error(Message.User.EMAIL_ALREADY_EXISTED);
//            throw new CustomException(new ErrorModel(400, Message.User.EMAIL_ALREADY_EXISTED));
//        }
        if (request.getShop() != null) {
            try {
                var shop = request.getShop();
                var shopExist = shopRepository.existsShopByShopName(shop.getShopName());
                var emailShopExist = shopRepository.existsShopByContactMail(shop.getContactMail());

                if (shopExist) {
                    log.error(Message.Shop.ALREADY_EXISTED);
                    throw new CustomException(new ErrorModel(400, Message.Shop.ALREADY_EXISTED));
                }
                if (emailShopExist) {
                    log.error(Message.Shop.EMAIL_ALREADY_EXISTED);
                    throw new CustomException(new ErrorModel(400, Message.Shop.EMAIL_ALREADY_EXISTED));
                }

                User user = userMapper.toUser(request);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setIsActive(false);
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByName(Constants.Authentication.SHOP_ROLE));
                user.setRoles(roles);
                log.info("Creating user ...");
                userRepository.save(user);

                log.info("Creating Shop");
                Shop shopEntity = shopMapper.toShop(shop);
                shopEntity.setIsActive(true);
                shopEntity.setUser(user);
                shopRepository.save(shopEntity);
                log.info("Send verify email ...");
                sendEmailUtil.sendVerifyEmail(user);
                return userMapper.toUserResponse(user);
            } catch (Exception e) {
                log.error("Error creating user!", e);
                throw new CustomException(new ErrorModel(400, Message.User.CREATE_FAILED));
            }
        } else {
            try {
                User user = userMapper.toUser(request);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setIsActive(false);
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByName(Constants.Authentication.USER_ROLE));
                user.setRoles(roles);
                log.info("Creating user ...");
                userRepository.save(user);
                log.info("Send verify email ...");
                sendEmailUtil.sendVerifyEmail(user);
                return userMapper.toUserResponse(user);
            } catch (Exception e) {
                log.error("Error creating user!", e);
                throw new CustomException(new ErrorModel(400, Message.User.CREATE_FAILED));
            }
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
