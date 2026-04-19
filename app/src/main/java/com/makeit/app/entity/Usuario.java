package com.makeit.app.entity;

import jakarta.persistence.*;

@Entity // Esto le dice a Spring que es una tabla de base de datos
@Table(name = "usuarios")
public class Usuario {

    @Id // Esta es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Se numera solo (1, 2, 3...)
    private Long id;

    private String nombre;
    private String email;
    private String password;

    // --- GETTERS Y SETTERS (Para que Java pueda leer los datos) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}