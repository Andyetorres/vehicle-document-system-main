package com.systemdocumentut.vehicle_document_system.Model.Config;

import java.io.Serializable;

public class JwtResponse implements Serializable {
    private final String jwttoken;

    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }
    public String getJwttoken() { return jwttoken; }
}