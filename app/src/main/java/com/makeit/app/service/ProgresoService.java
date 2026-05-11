package com.makeit.app.service;

import com.makeit.app.dto.progress.DailyProgressPoint;
import com.makeit.app.dto.progress.ProgressSummaryResponse;
import com.makeit.app.model.ProgresoDiario;
import com.makeit.app.model.Usuario;
import com.makeit.app.repository.ProgresoDiarioRepository;
import com.makeit.app.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProgresoService {

    private final UsuarioRepository usuarioRepository;
    private final ProgresoDiarioRepository progresoDiarioRepository;

    public ProgresoService(UsuarioRepository usuarioRepository, ProgresoDiarioRepository progresoDiarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.progresoDiarioRepository = progresoDiarioRepository;
    }

    @Transactional(readOnly = true)
    public ProgressSummaryResponse getResumen(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        List<ProgresoDiario> historial = progresoDiarioRepository.findByUsuarioOrderByFechaAsc(usuario);
        long totalAsignados = historial.size();
        long totalCompletados = progresoDiarioRepository.countByUsuarioAndCompletadoTrue(usuario);
        long totalPendientes = totalAsignados - totalCompletados;
        double tasa = totalAsignados == 0 ? 0.0 : (totalCompletados * 100.0) / totalAsignados;

        List<DailyProgressPoint> serie = historial.stream()
                .map(item -> new DailyProgressPoint(item.getFecha(), Boolean.TRUE.equals(item.getCompletado())))
                .toList();

        return new ProgressSummaryResponse(totalAsignados, totalCompletados, totalPendientes, tasa, serie);
    }
}
