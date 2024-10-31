package com.dev.identity.controller;

import com.dev.identity.dto.request.IntrospectRequest;
import com.dev.identity.dto.response.IntrospectResponse;
import com.dev.identity.entity.User;
import com.dev.identity.service.AuthenticationService;
import com.dev.identity.service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;

@Controller
@RequestMapping("/verify")
@RequiredArgsConstructor
public class TemplateController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @GetMapping()
    public String verifyValue(@RequestParam String token, @RequestParam Integer userId) throws ParseException, JOSEException {

        IntrospectResponse response = authenticationService.introspect(IntrospectRequest.builder().token(token).build());
        if (response.isValid()) {
            Boolean value = userService.updateUserStatus(userId);
            if (value) {
                return "verify_success";
            }
            return "verify_failure";
        }
        return "verify_failure";
    }
}
