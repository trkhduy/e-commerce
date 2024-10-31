package com.dev.configuration;

import com.dev.commons.Message;
import com.dev.commons.response.DataResponse;
import com.dev.commons.response.ResultModel;
import com.dev.constant.Constants;
import com.dev.dto.response.IntrospectResponse;
import com.dev.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {

    IdentityService identityService;
    ObjectMapper objectMapper;

    @NonFinal
    private String[] AUTH_WHITELIST = {
            "/users/register",
            "/identity/login",
            "/identity/introspect",
            "/identity/logout",
            "/identity/refreshToken",
            "/Category"
    };

    @Value("${app.api-prefix}")
    @NonFinal
    private String apiPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter ...");

        if (isAuthWhite(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        log.info("Not auth white list ...");
        // Lấy header Authorization
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(Constants.Authentication.BEARER_TOKEN_PREFIX)) {
            return unauthenticated(exchange.getResponse());
        }

        // Lấy token từ header
        String token = authHeader.replace(Constants.Authentication.BEARER_TOKEN_PREFIX, "");
        log.info("Token: {}", token);

        return identityService.introspect(token).flatMap(result -> {
            try {
                DataResponse dataResponse = objectMapper.convertValue(result.getBody(), DataResponse.class);
                if (dataResponse.isStatus()) {
                    IntrospectResponse introspectResponse = objectMapper.convertValue(dataResponse.getResult().getContent(), IntrospectResponse.class);
                    if (introspectResponse.isValid()) {
                        return chain.filter(exchange);
                    }
                }
            } catch (Exception e) {
                log.error("Error during token introspection: {}", e.getMessage());
            }
            return unauthenticated(exchange.getResponse());
        }).onErrorResume(throwable -> {
            log.error("Error in authentication filter: {}", throwable.getMessage());
            return unauthenticated(exchange.getResponse());
        });
    }

    private boolean isAuthWhite(ServerHttpRequest request) {
        log.info("Path URI: {}", request.getURI().getPath());
        return Arrays.stream(AUTH_WHITELIST)
                .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
    }


    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {

        DataResponse dataResponse = new DataResponse(true, new ResultModel<>(null, Message.Authentication.UNAUTHENTICATED));
        ResponseEntity<?> responseEntity = new ResponseEntity<>(dataResponse, HttpStatus.UNAUTHORIZED);
        String body;

        try {
            body = objectMapper.writeValueAsString(responseEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(
                Mono.just((response.bufferFactory().wrap(body.getBytes()))));
    }
}
