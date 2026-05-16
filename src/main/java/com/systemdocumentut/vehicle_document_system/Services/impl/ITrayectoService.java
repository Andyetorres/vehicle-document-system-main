package com.systemdocumentut.vehicle_document_system.Services.impl;

import com.systemdocumentut.vehicle_document_system.Model.Trayecto;
import java.util.List;
import java.util.Map;

public interface ITrayectoService {

    /**
     * Registra un nuevo trayecto o parada en la base de datos[cite: 14, 16].
     * Condición: Los documentos del vehículo deben estar "Habilitado" y el 
     * conductor en estado apto para operar ("PO").
     */
    Trayecto guardarTrayecto(Trayecto trayecto);

    // --- REQUERIMIENTOS DE SERVICIOS DE CONSULTA ---

    /**
     * REQUERIMIENTO 1: Servicio protegido que visualiza en orden los trayectos 
     * de una ruta específica[cite: 38].
     * @param codigoRuta Código único que agrupa los trayectos[cite: 21, 38].
     */
    List<Trayecto> consultarTrayectosPorCodigoRuta(String codigoRuta);

    /**
     * REQUERIMIENTO 2: Servicio protegido que visualiza de forma agrupada 
     * los códigos de rutas de un conductor específico[cite: 39, 40].
     * @param identificacion Número de documento del conductor[cite: 39].
     */
    List<String> consultarCodigosRutaPorConductor(String identificacion);

    /**
     * REQUERIMIENTO 3: Servicio protegido que visualiza el código de ruta y el 
     * conductor asociado al trayecto de forma agrupada por placa[cite: 41, 42].
     * Retorna una estructura agrupada (ej. Map o DTO personalizado)[cite: 41].
     */
    Map<String, Object> consultarRutaYConductorPorPlaca(String placa);

    /**
     * REQUERIMIENTO 4: Servicio de consulta de rutas e información de trayectos 
     * donde el vehículo NO esté habilitado o el conductor esté Restringido para Operar (RO)[cite: 42].
     */
    List<Trayecto> consultarTrayectosConRestricciones();

    // --- REQUERIMIENTO TÉCNICO (SOPORTE CRON JOB) ---

    /**
     * Tarea programada (90s): Obtiene los trayectos que no tienen coordenadas (latitud/longitud).
     */
    List<Trayecto> listarTrayectosSinCoordenadas();

    /**
     * Actualiza la latitud y longitud de un trayecto específico tras consultar la API de Google Maps.
     */
    void actualizarCoordenadas(Long trayectoId, Double latitud, Double longitud);
}