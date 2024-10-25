package com.dev.configuration;

import com.dev.commons.Message;
import com.dev.commons.response.DataResponse;
import com.dev.constant.Constants;
import com.dev.dto.response.IntrospectResponse;
import com.dev.service.IdentityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {

    IdentityService identityService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter ...");
        List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty((authHeaders)))
            return unauthenticated(exchange.getResponse());

        String token = authHeaders.getFirst().replace(Constants.Authentication.BEARER_TOKEN_PREFIX, "");
        log.info("Token: {}", token);

        ObjectMapper objectMapper = new ObjectMapper();

        identityService.introspect(token).subscribe(result -> {
            try {
                DataResponse dataResponse = objectMapper.convertValue(result.getBody(), DataResponse.class);

                if (dataResponse.isStatus()) {
                    IntrospectResponse introspectResponse = objectMapper.convertValue(dataResponse.getResult().getContent(), IntrospectResponse.class);
                    System.out.println("Valid token: " + introspectResponse.isValid());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        String body = Message.Authentication.UNAUTHENTICATED;
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(
                Mono.just((response.bufferFactory().wrap(body.getBytes()))));
    }
}
