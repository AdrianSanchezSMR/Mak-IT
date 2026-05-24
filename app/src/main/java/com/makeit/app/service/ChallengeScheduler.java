package com.makeit.app.service;

import com.makeit.app.model.Usuario;
import com.makeit.app.repository.UsuarioRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ChallengeScheduler {

    private final UsuarioRepository usuarioRepository;
    private final ChallengeService challengeService;

    public ChallengeScheduler(UsuarioRepository usuarioRepository, ChallengeService challengeService) {
        this.usuarioRepository = usuarioRepository;
        this.challengeService = challengeService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void generateDueChallenges() {
        LocalDate today = LocalDate.now();
        List<Usuario> usuarios = usuarioRepository.findByHoraAvisoIsNotNull();
        for (Usuario usuario : usuarios) {
            challengeService.generateDueChallengeForUser(usuario, today);
        }
    }
}
