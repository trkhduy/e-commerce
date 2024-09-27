package com.dev.identity.controller;

import com.dev.commons.Message;
import com.dev.commons.response.DataResponse;
import com.dev.commons.response.ResultModel;
import com.dev.identity.dto.request.RoleRequest;
import com.dev.identity.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    ResponseEntity<?> create(@RequestBody RoleRequest request) {
        DataResponse response = new DataResponse();
        response.setStatus(true);
        response.setResult(new ResultModel<>(null, roleService.create(request)));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<?> getAll() {
        DataResponse response = new DataResponse();
        response.setStatus(true);
        response.setResult(new ResultModel<>(null, roleService.getAll()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{role}")
    ResponseEntity<?> delete(@PathVariable String role) {
        roleService.delete(role);
        DataResponse response = new DataResponse();
        response.setStatus(true);
        response.setResult(new ResultModel<>(null, Message.Permission.DELETE));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
