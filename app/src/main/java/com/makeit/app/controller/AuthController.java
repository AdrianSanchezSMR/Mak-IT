package com.makeit.app.controller;

import com.makeit.app.dto.auth.AuthResponse;
import com.makeit.app.dto.auth.LoginRequest;
import com.makeit.app.dto.auth.MeResponse;
import com.makeit.app.dto.auth.RegisterRequest;
import com.makeit.app.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public MeResponse me(@AuthenticationPrincipal String username) {
        return authService.me(username);
    }
}
