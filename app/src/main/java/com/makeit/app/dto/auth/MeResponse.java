package com.makeit.app.dto.auth;

import java.time.LocalTime;

public class MeResponse {

    private Long id;
    private String username;
    private String email;
    private LocalTime horaAviso;

    public MeResponse(Long id, String username, String email, LocalTime horaAviso) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.horaAviso = horaAviso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalTime getHoraAviso() {
        return horaAviso;
    }

    public void setHoraAviso(LocalTime horaAviso) {
        this.horaAviso = horaAviso;
    }
}
