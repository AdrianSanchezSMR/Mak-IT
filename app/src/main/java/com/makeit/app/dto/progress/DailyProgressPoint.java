package com.makeit.app.dto.progress;

import java.time.LocalDate;

public class DailyProgressPoint {

    private LocalDate fecha;
    private boolean completado;

    public DailyProgressPoint(LocalDate fecha, boolean completado) {
        this.fecha = fecha;
        this.completado = completado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }
}
