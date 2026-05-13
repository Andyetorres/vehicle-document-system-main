package com.systemdocumentut.vehicle_document_system.Services.impl;

import com.systemdocumentut.vehicle_document_system.Model.Persona;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interfaz que define el contrato para la gestión de personas y 
 * operaciones de seguridad asociadas a usuarios administrativos.
 */
public interface IPersonaService {

    // --- MÉTODOS DE PERSISTENCIA (CRUD) ---

    Persona crearPersona(Persona persona);

    List<Persona> listarTodas();

    Optional<Persona> buscarPorId(Long id);

    /**
     * Actualiza los datos de una persona existente.
     * Debe coincidir con la firma en PersonaServiceImpl
     */
    Persona actualizar(Long id, Persona datosNuevos);

    /**
     * Elimina una persona por su ID.
     * Retorna true si fue eliminada, false si no existía.
     */
    boolean eliminar(Long id);

    // --- MÉTODOS DE SEGURIDAD Y ESTADÍSTICAS ---

    Map<String, Long> obtenerEstadisticas();

    void actualizarPassword(String login, String newPassword);

    String regenerarApiKey(String login);
}