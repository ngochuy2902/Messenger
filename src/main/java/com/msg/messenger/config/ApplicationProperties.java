package com.msg.messenger.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private Jwt jwt;
    private Redis redis;
    private int cookieValidityInSeconds;

    @Getter
    @Setter
    public static class Jwt {
        private String secretKey;
        private long tokenValidityInSeconds;
    }

    @Getter
    @Setter
    public static class Redis {
        private String hostName;
        private int port;
    }
}
