package com.systemdocumentut.vehicle_document_system.Model.Config;

import java.io.Serializable;

/**
 * DTO para enviar la respuesta de autenticación exitosa.
 * Contiene el token JWT generado tras validar:
 * 1. X-API-KEY en el header.
 * 2. Login y Password en el body.
 * 3. Tipo de persona ADMINISTRATIVO ('A').
 */
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final String jwttoken;

    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getJwttoken() {
        return this.jwttoken;
    }
}