package com.makeit.app.dto.progress;

import java.util.List;

public class ProgressSummaryResponse {

    private long totalRetosAsignados;
    private long totalRetosCompletados;
    private long totalRetosPendientes;
    private double tasaCompletado;
    private List<DailyProgressPoint> serieDiaria;

    public ProgressSummaryResponse(
            long totalRetosAsignados,
            long totalRetosCompletados,
            long totalRetosPendientes,
            double tasaCompletado,
            List<DailyProgressPoint> serieDiaria
    ) {
        this.totalRetosAsignados = totalRetosAsignados;
        this.totalRetosCompletados = totalRetosCompletados;
        this.totalRetosPendientes = totalRetosPendientes;
        this.tasaCompletado = tasaCompletado;
        this.serieDiaria = serieDiaria;
    }

    public long getTotalRetosAsignados() {
        return totalRetosAsignados;
    }

    public void setTotalRetosAsignados(long totalRetosAsignados) {
        this.totalRetosAsignados = totalRetosAsignados;
    }

    public long getTotalRetosCompletados() {
        return totalRetosCompletados;
    }

    public void setTotalRetosCompletados(long totalRetosCompletados) {
        this.totalRetosCompletados = totalRetosCompletados;
    }

    public long getTotalRetosPendientes() {
        return totalRetosPendientes;
    }

    public void setTotalRetosPendientes(long totalRetosPendientes) {
        this.totalRetosPendientes = totalRetosPendientes;
    }

    public double getTasaCompletado() {
        return tasaCompletado;
    }

    public void setTasaCompletado(double tasaCompletado) {
        this.tasaCompletado = tasaCompletado;
    }

    public List<DailyProgressPoint> getSerieDiaria() {
        return serieDiaria;
    }

    public void setSerieDiaria(List<DailyProgressPoint> serieDiaria) {
        this.serieDiaria = serieDiaria;
    }
}
