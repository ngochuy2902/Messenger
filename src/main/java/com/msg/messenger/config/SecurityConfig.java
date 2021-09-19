package com.msg.messenger.config;

import com.msg.messenger.enums.RoleType;
import com.msg.messenger.security.jwt.JwtConfigurer;
import com.msg.messenger.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig
    extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;

    public SecurityConfig(final TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity http)
        throws Exception {
        http.cors();
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            .antMatchers("/api/user/**").authenticated()
            .antMatchers("/api/admin/**").hasAuthority(RoleType.ADMIN.name());

        http.apply(securityConfigurer());
    }

    private JwtConfigurer securityConfigurer() {
        return new JwtConfigurer(tokenProvider);
    }
}
