package com.dev.identity.controller;

import com.dev.commons.Message;
import com.dev.commons.response.DataResponse;
import com.dev.commons.response.ResultModel;
import com.dev.identity.dto.request.UserCreationRequest;
import com.dev.identity.dto.request.UserUpdateRequest;
import com.dev.identity.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('GET_ALL_USER')")
    @GetMapping()
    public ResponseEntity<?> getAllUser() {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, userService.getAllUser()));
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreationRequest request) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, userService.createUser(request)));
        return ResponseEntity.ok(dataResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, userService.updateUser(request, id)));
        return ResponseEntity.ok(dataResponse);
    }

    @PostAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, userService.getUserById(id)));
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/myInfo")
    public ResponseEntity<?> getMyInfo() {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, userService.getMyInfo()));
        return ResponseEntity.ok(dataResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, Message.User.DELETE));
        return ResponseEntity.ok(dataResponse);
    }
}
