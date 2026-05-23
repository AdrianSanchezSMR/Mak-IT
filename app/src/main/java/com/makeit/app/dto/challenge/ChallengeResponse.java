package com.makeit.app.dto.challenge;

import java.time.LocalDate;

public class ChallengeResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private Long categoriaId;
    private String categoriaNombre;
    private boolean completadoHoy;
    private LocalDate fecha;

    public ChallengeResponse(
            Long id,
            String titulo,
            String descripcion,
            Long categoriaId,
            String categoriaNombre,
            boolean completadoHoy,
            LocalDate fecha
    ) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
        this.completadoHoy = completadoHoy;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public boolean isCompletadoHoy() {
        return completadoHoy;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
