package com.msg.messenger.api.admin;

import com.msg.messenger.bloc.AuthBloc;
import com.msg.messenger.dto.request.AuthReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController("adminAuthController")
@RequestMapping("/api/auth/admin")
public class AuthController {

    private final AuthBloc authBloc;

    public AuthController(final AuthBloc authBloc) {
        this.authBloc = authBloc;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody final AuthReq authReq,
                                   final HttpServletResponse response) {

        Cookie cookie = authBloc.loginAdmin(authReq);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(final HttpServletRequest request,
                                    final HttpServletResponse response) {
        authBloc.logout(request, response);

        return ResponseEntity.noContent().build();
    }
}
