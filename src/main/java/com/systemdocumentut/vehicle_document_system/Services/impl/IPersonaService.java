package com.systemdocumentut.vehicle_document_system.Services.impl;

import com.systemdocumentut.vehicle_document_system.Model.Persona;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interfaz que define el contrato para la gestión de personas y 
 * operaciones de seguridad asociadas a usuarios administrativos[cite: 19].
 */
public interface IPersonaService {

    // --- MÉTODOS DE PERSISTENCIA (CRUD) ---

    Persona crearPersona(Persona persona);
    List<Persona> listarTodas();
    Optional<Persona> buscarPorId(Long id);
    Persona actualizar(Long id, Persona datosNuevos);
    boolean eliminar(Long id);

    // --- NUEVOS REQUERIMIENTOS: CONDUCTORES Y LICENCIAS (ENTREGA 3) ---

    void actualizarLicenciaConductor(Long personaId, String licenciaBase64, LocalDate fechaVigencia);
    List<Persona> listarConductoresConLicenciaVencida();
    void restringirConductorPorLicenciaVencida(Long personaId);

    // --- MÉTODOS DE SEGURIDAD Y ESTADÍSTICAS ---

    Map<String, Long> obtenerEstadisticas();
    void actualizarPassword(String login, String newPassword);
    String regenerarApiKey(String login);
}