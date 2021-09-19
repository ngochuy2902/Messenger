package com.msg.messenger.security.jwt;

import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static com.msg.messenger.constant.Constant.ACCESS_TOKEN;

@Slf4j
@Component
public class JwtFilter
    extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    public JwtFilter(final TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwt = getJwt(httpServletRequest);
        if (Strings.hasText(jwt)) {
            try {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        chain.doFilter(request, response);
    }

    private String getJwt(final HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();

        return Objects.isNull(cookies) ? null
            : Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(ACCESS_TOKEN))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
    }
}
