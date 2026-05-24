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
import java.util.Map;
import java.util.stream.Collectors;

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

        Map<java.time.LocalDate, Boolean> completadoPorFecha = historial.stream()
                .collect(Collectors.groupingBy(
                        ProgresoDiario::getFecha,
                        java.util.TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                items -> {
                                    boolean anyCompleted = items.stream().anyMatch(item -> Boolean.TRUE.equals(item.getCompletado()));
                                    boolean isToday = items.stream().anyMatch(item -> item.getFecha().equals(java.time.LocalDate.now()));
                                    return anyCompleted || isToday;
                                }
                        )
                ));

        List<DailyProgressPoint> serie = completadoPorFecha.entrySet().stream()
                .map(item -> new DailyProgressPoint(item.getKey(), item.getValue()))
                .toList();

        return new ProgressSummaryResponse(totalAsignados, totalCompletados, totalPendientes, tasa, serie);
    }
}
