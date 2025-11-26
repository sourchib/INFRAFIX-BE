package com.infrafix.citizen_reporting.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:jwt.properties")
public class JwtConfig {

    private static Long timeExpiration;
    private static String tokenEncryptEnable;


    public static String getTokenEncryptEnable() {
        return tokenEncryptEnable;
    }

    @Value("${token.enable.encrypt}")
    private void setTokenEncryptEnable(String tokenEncryptEnable) {
        this.tokenEncryptEnable = tokenEncryptEnable;
    }

    public static Long getTimeExpiration() {
        return timeExpiration;
    }

    @Value("${time.expiration}")
    private void setTimeExpiration(Long timeExpiration) {
        JwtConfig.timeExpiration = timeExpiration;
    }
}