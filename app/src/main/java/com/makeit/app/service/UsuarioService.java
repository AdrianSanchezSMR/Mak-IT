package com.makeit.app.service;

import com.makeit.app.dto.auth.MeResponse;
import com.makeit.app.dto.user.ChangePasswordRequest;
import com.makeit.app.dto.user.UpdateProfileRequest;
import com.makeit.app.model.Usuario;
import com.makeit.app.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MeResponse getProfile(String username) {
        Usuario usuario = findByUsernameOrThrow(username);
        return toMeResponse(usuario);
    }

    public MeResponse updateProfile(String username, UpdateProfileRequest request) {
        Usuario usuario = findByUsernameOrThrow(username);

        String normalizedUsername = request.getUsername().trim();
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (!usuario.getUsername().equals(normalizedUsername) && usuarioRepository.existsByUsername(normalizedUsername)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El username ya existe");
        }

        if (!usuario.getEmail().equalsIgnoreCase(normalizedEmail) && usuarioRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya existe");
        }

        usuario.setUsername(normalizedUsername);
        usuario.setEmail(normalizedEmail);
        usuario.setHoraAviso(request.getHoraAviso());

        Usuario updated = usuarioRepository.save(usuario);
        return toMeResponse(updated);
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        Usuario usuario = findByUsernameOrThrow(username);

        if (!passwordEncoder.matches(request.getCurrentPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La contraseña actual es incorrecta");
        }

        if (passwordEncoder.matches(request.getNewPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nueva contraseña debe ser diferente");
        }

        usuario.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usuarioRepository.save(usuario);
    }

    private Usuario findByUsernameOrThrow(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    private MeResponse toMeResponse(Usuario usuario) {
        return new MeResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getHoraAviso(),
                usuario.getRole().name()
        );
    }
}
