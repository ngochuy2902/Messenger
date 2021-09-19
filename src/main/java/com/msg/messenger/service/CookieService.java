package com.msg.messenger.service;

import com.msg.messenger.config.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static com.msg.messenger.constant.Constant.ACCESS_TOKEN;

@Slf4j
@Service
public class CookieService {

    private final int cookieValidityInSeconds;

    public CookieService(final ApplicationProperties applicationProperties) {
        this.cookieValidityInSeconds = applicationProperties.getCookieValidityInSeconds();
    }

    public Cookie createAccessToken(final String jwt) {
        log.info("Create access token cookie by jwt #{}", jwt);

        Cookie cookie = new Cookie(ACCESS_TOKEN, jwt);
        cookie.setMaxAge(cookieValidityInSeconds);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    public void removeAccessToken(final HttpServletResponse response) {
        log.info("Remove access token cookie");

        Cookie cookie = new Cookie(ACCESS_TOKEN, "");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
    }
}
