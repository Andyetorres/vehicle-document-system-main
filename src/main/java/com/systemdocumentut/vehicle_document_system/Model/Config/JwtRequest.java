package com.systemdocumentut.vehicle_document_system.Model.Config;

import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;

public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "El login de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    public JwtRequest() {}

    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}