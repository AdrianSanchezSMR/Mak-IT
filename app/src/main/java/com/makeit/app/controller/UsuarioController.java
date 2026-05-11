package com.makeit.app.controller;

import com.makeit.app.dto.auth.MeResponse;
import com.makeit.app.dto.user.ChangePasswordRequest;
import com.makeit.app.dto.user.UpdateProfileRequest;
import com.makeit.app.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/profile")
    public MeResponse getMyProfile(@AuthenticationPrincipal String username) {
        return usuarioService.getProfile(username);
    }

    @PutMapping("/profile")
    public MeResponse updateMyProfile(
            @AuthenticationPrincipal String username,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return usuarioService.updateProfile(username, request);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @AuthenticationPrincipal String username,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        usuarioService.changePassword(username, request);
    }
}
