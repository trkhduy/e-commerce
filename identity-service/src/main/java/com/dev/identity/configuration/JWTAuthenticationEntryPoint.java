package com.dev.identity.configuration;

import com.dev.commons.response.DataResponse;
import com.dev.commons.response.ErrorModel;
import com.dev.commons.response.ResultModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("Unauthorized ...", authException);
        ErrorModel errorModel = new ErrorModel(401, authException.getMessage());

        DataResponse dataResponse = DataResponse.builder()
                .status(false)
                .result(ResultModel.builder()
                        .content(errorModel)
                        .build())
                .build();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(dataResponse);
        response.getWriter().write(jsonResponse);
        response.flushBuffer();
    }
}
