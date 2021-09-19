package com.msg.messenger.security.jwt;

import com.msg.messenger.config.ApplicationProperties;
import com.msg.messenger.security.CustomUserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private static final String AUTHORITIES = "auth";
    private static final String USER_ID = "userId";
    private final ApplicationProperties.Jwt jwtProperty;
    private final JwtParser jwtParser;
    private final Key key;

    public TokenProvider(final ApplicationProperties applicationProperties) {
        String secretKey = applicationProperties.getJwt().getSecretKey();
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.jwtProperty = applicationProperties.getJwt();
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();;
    }

    public String createToken(final Authentication authentication) {
        String authorities = authentication.getAuthorities()
                                           .stream()
                                           .map(GrantedAuthority::getAuthority)
                                           .collect(Collectors.joining(","));
        long now = new Date().getTime();
        Date validity = new Date(now + jwtProperty.getTokenValidityInSeconds() * 1000);
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        return Jwts.builder()
                   .setSubject(authentication.getName())
                   .claim(USER_ID, customUserDetail.getId())
                   .claim(AUTHORITIES, authorities)
                   .signWith(key, SignatureAlgorithm.HS512)
                   .setIssuedAt(new Date())
                   .setExpiration(validity)
                   .compact();
    }

    public Authentication getAuthentication(final String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES).toString().split(","))
                  .filter(auth -> !auth.trim().isEmpty())
                  .map(SimpleGrantedAuthority::new)
                  .collect(Collectors.toList());
        CustomUserDetail customUserDetail = CustomUserDetail.builder()
                                                            .id(Long.parseLong(claims.get(USER_ID).toString()))
                                                            .username(claims.getSubject())
                                                            .authorities(authorities)
                                                            .build();
        return new UsernamePasswordAuthenticationToken(customUserDetail, token, authorities);
    }
}
