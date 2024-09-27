package com.dev.identity.controller;

import com.dev.commons.Message;
import com.dev.commons.response.DataResponse;
import com.dev.commons.response.ResultModel;
import com.dev.identity.dto.request.PermissionRequest;
import com.dev.identity.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ResponseEntity<?> create(@RequestBody PermissionRequest request) {
        DataResponse response = new DataResponse();
        response.setStatus(true);
        response.setResult(new ResultModel<>(null, permissionService.create(request)));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<?> getAll() {
        DataResponse response = new DataResponse();
        response.setStatus(true);
        response.setResult(new ResultModel<>(null, permissionService.getAll()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable String id) {
        permissionService.delete(id);
        DataResponse response = new DataResponse();
        response.setStatus(true);
        response.setResult(new ResultModel<>(null, Message.Permission.DELETE));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
