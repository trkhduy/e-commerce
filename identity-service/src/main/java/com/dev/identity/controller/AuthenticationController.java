package com.dev.identity.controller;

import com.dev.commons.response.DataResponse;
import com.dev.commons.response.ResultModel;
import com.dev.identity.dto.request.AuthenticationRequest;
import com.dev.identity.dto.request.IntrospectRequest;
import com.dev.identity.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/token")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, authenticationService.authenticate(request)));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping("/introspect")
    public ResponseEntity<?> introspect(@RequestBody @Valid IntrospectRequest request) throws ParseException, JOSEException {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, authenticationService.introspect(request)));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }


}
