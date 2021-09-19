package com.msg.messenger.bloc;

import com.msg.messenger.domain.User;
import com.msg.messenger.domain.UserRole;
import com.msg.messenger.dto.request.AuthReq;
import com.msg.messenger.exception.ValidatorException;
import com.msg.messenger.helper.SecurityHelper;
import com.msg.messenger.security.jwt.TokenProvider;
import com.msg.messenger.service.CookieService;
import com.msg.messenger.service.UserRoleService;
import com.msg.messenger.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.msg.messenger.enums.RoleType.ADMIN;
import static com.msg.messenger.enums.RoleType.USER;

@Slf4j
@Service
public class AuthBloc {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final CookieService cookieService;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;

    public AuthBloc(final AuthenticationManagerBuilder authenticationManagerBuilder,
                    final CookieService cookieService,
                    final TokenProvider tokenProvider,
                    final UserService userService,
                    final UserRoleService userRoleService,
                    final PasswordEncoder passwordEncoder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.cookieService = cookieService;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
    }

    public Cookie loginAdmin(final AuthReq authReq) {
        log.info("Login admin with username #{}", authReq.getUsername());

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (!SecurityHelper.hasRole(ADMIN)) {
            throw new ValidatorException("invalid_role", "user");
        }

        return cookieService.createAccessToken(tokenProvider.createToken(authentication));
    }

    public Cookie login(final AuthReq authReq) {
        log.info("Login with username #{}", authReq.getUsername());

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return cookieService.createAccessToken(tokenProvider.createToken(authentication));
    }

    public void logout(final HttpServletRequest request,
                       final HttpServletResponse response) {
        SecurityContextLogoutHandler contextHandler = new SecurityContextLogoutHandler();
        contextHandler.logout(request, response, null);
        cookieService.removeAccessToken(response);
    }

    @Transactional
    public void register(final AuthReq authReq) {
        log.info("Register with username #{}", authReq.getUsername());

        User user = userService.getByUsername(authReq.getUsername());

        if (ObjectUtils.anyNotNull(user)) {
            throw new ValidatorException("username_already_exist", "user");
        }

        registerUser(authReq);
    }

    private void registerUser(final AuthReq authReq) {
        User user = User.builder()
                        .username(authReq.getUsername())
                        .password(passwordEncoder.encode(authReq.getPassword()))
                        .build();
        userService.save(user);

        UserRole userRole = UserRole.builder()
                                    .userId(user.getId())
                                    .roleId(USER.getRoleId())
                                    .build();
        userRoleService.save(userRole);
    }
}
