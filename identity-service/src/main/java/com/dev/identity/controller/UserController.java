package com.dev.identity.controller;

import com.dev.commons.response.DataResponse;
import com.dev.commons.response.ResultModel;
import com.dev.identity.dto.request.UserRequest;
import com.dev.identity.dto.response.UserResponse;
import com.dev.identity.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllUser() {
        DataResponse dataResponse = new DataResponse();
        try {
            dataResponse.setStatus(true);
            dataResponse.setResult(new ResultModel<>(null, userService.getAllUser()));
            return ResponseEntity.ok(dataResponse);
        } catch (Exception e) {
            log.info("Failed to get all users", e);
            dataResponse.setStatus(false);
            dataResponse.setResult(new ResultModel<>(null, e.getMessage()));
            return ResponseEntity.ok(dataResponse);
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest request) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, userService.createUser(request)));
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }
}
