package com.systemdocumentut.vehicle_document_system.Services.impl;

import com.systemdocumentut.vehicle_document_system.DTOs.*;
import java.util.List;

/**
 * Interfaz que define los servicios para la gestión de vehículos,
 * documentos en Base64 y asociación de conductores.
 */
public interface IVehiculoService {

    // --- CRUD BÁSICO ---
    List<VehiculoDTO> listarTodos();
    VehiculoDTO buscarPorId(Long id);
    VehiculoDTO actualizar(Long id, VehiculoDTO dto);
    void eliminar(Long id);

    // --- REQUERIMIENTOS DE DOCUMENTOS ---
    
    // Crear vehículo con un documento inicial (Estado: EA - Espera de Aprobación/Verificación)
    VehiculoDTO crearVehiculoConDocumento(VehiculoDTO dto, Long idDocumentoBase);

    // Cargar o actualizar documentos (uno o varios) en formato Base64 (Requerimiento PDF)
    void cargarDocumentosBase64(Long vehiculoId, List<DocumentoCargaDTO> documentos);

    // --- REQUERIMIENTOS DE CONDUCTORES ---

    // Asociar conductores a un vehículo (Validando que sean tipo 'C')
    void asociarConductor(Long vehiculoId, Long personaId);

    // Cambiar estado de la relación conductor-vehículo (PO, EA, RO)
    void actualizarEstadoConductor(Long vehiculoId, Long personaId, String nuevoEstado);

    // --- CONSULTAS Y BÚSQUEDAS (Servicios Públicos y Privados) ---

    VehiculoDTO buscarPorPlaca(String placa);
    
    // Consulta vehículo por placa detallando conductores asociados y documentos
    VehiculoDetalleDTO obtenerDetallePorPlaca(String placa);

    List<VehiculoDTO> buscarPorTipoVehiculo(String tipo);
    
    List<VehiculoDTO> buscarPorEstadoDocumento(String estado);
    
    List<VehiculoDTO> buscarVehiculosPorTipoDocumento(Long idDocumento);

    // Consultar vehículos con documentos vencidos
    List<VehiculoDTO> consultarVehiculosConDocumentosVencidos();

    // Consultar vehículos con documentos por vencer en un tiempo X (parámetro días)
    List<VehiculoDTO> consultarVehiculosPorVencer(int dias);

    // --- AUXILIARES ---
    void agregarDocumentoAVehiculo(Long vehiculoId, Long documentoId, String fechaExp, String fechaVen);
}