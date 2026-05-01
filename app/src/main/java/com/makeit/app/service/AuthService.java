package com.makeit.app.service;

import com.makeit.app.dto.auth.AuthResponse;
import com.makeit.app.dto.auth.LoginRequest;
import com.makeit.app.dto.auth.MeResponse;
import com.makeit.app.dto.auth.RegisterRequest;
import com.makeit.app.model.Usuario;
import com.makeit.app.repository.UsuarioRepository;
import com.makeit.app.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El username ya existe");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setHoraAviso(request.getHoraAviso());

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario.getUsername());
        return new AuthResponse(token, usuario.getUsername());
    }

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas"));

        boolean passwordOk = passwordEncoder.matches(request.getPassword(), usuario.getPassword());
        if (!passwordOk) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        String token = jwtService.generateToken(usuario.getUsername());
        return new AuthResponse(token, usuario.getUsername());
    }

    public MeResponse me(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return new MeResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getHoraAviso()
        );
    }
}
