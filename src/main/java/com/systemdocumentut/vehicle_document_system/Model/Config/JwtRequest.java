package com.systemdocumentut.vehicle_document_system.Model.Config;

import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;

/**
 * Clase DTO para capturar las credenciales de inicio de sesión.
 * Cumple con el requerimiento de transportar el 'login' (nemotecnia)
 * y el password para la validación de usuarios ADMINISTRATIVOS.
 */
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "El nombre de usuario (login) es obligatorio")
    private String username; // Este campo recibirá el login generado (Ej: JP12345)

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    // Constructor por defecto necesario para la deserialización de JSON
    public JwtRequest() {
    }

    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}