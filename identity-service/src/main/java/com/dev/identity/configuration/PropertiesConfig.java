package com.dev.identity.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class PropertiesConfig {
    @Value("${token.secret.key}")
    private String secretKey;

    @Value("${token.expiration.time}")
    private Integer expirationTime;

    @Value("${token.verify-mail.expiration.time}")
    private Integer verifyMailTokenTime;

    @Value("${token.refresh.expiration.time}")
    private Integer refreshTokenTime;
}
