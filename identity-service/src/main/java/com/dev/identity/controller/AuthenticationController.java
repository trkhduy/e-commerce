package com.dev.identity.controller;

import com.dev.commons.Message;
import com.dev.commons.response.DataResponse;
import com.dev.commons.response.ResultModel;
import com.dev.identity.dto.request.AuthenticationRequest;
import com.dev.identity.dto.request.IntrospectRequest;
import com.dev.identity.dto.request.LogoutRequest;
import com.dev.identity.dto.request.RefreshRequest;
import com.dev.identity.dto.response.IntrospectResponse;
import com.dev.identity.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, authenticationService.authenticate(request)));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, authenticationService.refreshToken(request)));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping("/introspect")
    public ResponseEntity<?> introspect(@RequestBody @Valid IntrospectRequest request) throws ParseException, JOSEException {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, authenticationService.introspect(request)));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, Message.Authentication.LOG_OUT));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("/verifyAccount")
    public String confirmEmail(@RequestParam("token") String token) throws ParseException, JOSEException {
        IntrospectResponse introspect = authenticationService.introspect(
                IntrospectRequest.builder()
                        .token(token)
                        .build());
        if (introspect.isValid()) {
            return "redirect:/verify_success.html";
        }
        return "redirect:/verify_failure.html";
    }


}
