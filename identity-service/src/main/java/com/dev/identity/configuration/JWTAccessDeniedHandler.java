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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class JWTAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.error("Access Denied ...", accessDeniedException);
        ErrorModel errorModel = new ErrorModel(403, accessDeniedException.getMessage());

        DataResponse dataResponse = DataResponse.builder()
                .status(false)
                .result(ResultModel.builder()
                        .content(errorModel)
                        .build())
                .build();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(dataResponse);
        response.getWriter().write(jsonResponse);
        response.flushBuffer();
    }
}
