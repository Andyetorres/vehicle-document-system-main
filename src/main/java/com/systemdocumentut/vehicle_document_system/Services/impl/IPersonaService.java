package com.systemdocumentut.vehicle_document_system.Services.impl;

import com.systemdocumentut.vehicle_document_system.Model.Persona;
import java.util.List;

public interface IPersonaService {
    Persona crearPersona(Persona persona);
    List<Persona> listarTodas();
    Persona actualizar(Long id, Persona datosNuevos);
}