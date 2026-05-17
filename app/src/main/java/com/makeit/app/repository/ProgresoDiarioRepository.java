package com.makeit.app.repository;

import com.makeit.app.model.ProgresoDiario;
import com.makeit.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProgresoDiarioRepository extends JpaRepository<ProgresoDiario, Long> {
    Optional<ProgresoDiario> findByUsuarioAndFecha(Usuario usuario, LocalDate fecha);
    List<ProgresoDiario> findByUsuarioAndFechaOrderByIdAsc(Usuario usuario, LocalDate fecha);
    Optional<ProgresoDiario> findByUsuarioAndRetoCatalogoIdAndFecha(Usuario usuario, Long retoId, LocalDate fecha);
    List<ProgresoDiario> findByUsuarioOrderByFechaAsc(Usuario usuario);
    long countByUsuarioAndCompletadoTrue(Usuario usuario);
}
