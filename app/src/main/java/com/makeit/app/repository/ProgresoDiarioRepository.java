package com.makeit.app.repository;

import com.makeit.app.model.ProgresoDiario;
import com.makeit.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProgresoDiarioRepository extends JpaRepository<ProgresoDiario, Long> {
    Optional<ProgresoDiario> findByUsuarioAndFecha(Usuario usuario, LocalDate fecha);

    @Query("""
            SELECT p FROM ProgresoDiario p
            JOIN FETCH p.retoCatalogo r
            JOIN FETCH r.categoria
            WHERE p.usuario = :usuario AND p.fecha = :fecha
            ORDER BY p.id ASC
            """)
    List<ProgresoDiario> findByUsuarioAndFechaOrderByIdAsc(
            @Param("usuario") Usuario usuario,
            @Param("fecha") LocalDate fecha
    );

    @Query("""
            SELECT p FROM ProgresoDiario p
            JOIN FETCH p.retoCatalogo r
            JOIN FETCH r.categoria
            WHERE p.usuario = :usuario AND p.completado = false
            ORDER BY p.fecha ASC, p.id ASC
            """)
    List<ProgresoDiario> findByUsuarioAndCompletadoFalseOrderByFechaAscIdAsc(@Param("usuario") Usuario usuario);

    @Query("""
            SELECT p FROM ProgresoDiario p
            JOIN FETCH p.retoCatalogo r
            JOIN FETCH r.categoria
            WHERE p.usuario = :usuario
            ORDER BY p.fecha DESC, p.id DESC
            """)
    List<ProgresoDiario> findByUsuarioWithRetoOrderByFechaDescIdDesc(@Param("usuario") Usuario usuario);

    Optional<ProgresoDiario> findByUsuarioAndRetoCatalogoIdAndFecha(Usuario usuario, Long retoId, LocalDate fecha);
    List<ProgresoDiario> findByUsuarioOrderByFechaAsc(Usuario usuario);
    long countByUsuarioAndCompletadoTrue(Usuario usuario);
}
