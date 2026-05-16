package com.systemdocumentut.vehicle_document_system.Services.impl;

import com.systemdocumentut.vehicle_document_system.DTOs.*;
import java.util.List;

public interface IVehiculoService {

    // --- CRUD BÁSICO ---
    List<VehiculoDTO> listarTodos();
    VehiculoDTO buscarPorId(Long id);
    VehiculoDTO actualizar(Long id, VehiculoDTO dto);
    void eliminar(Long id);

    // --- REQUERIMIENTOS DE DOCUMENTOS ---
    VehiculoDTO crearVehiculoConDocumento(VehiculoDTO dto, Long idDocumentoBase);
    void cargarDocumentosBase64(Long vehiculoId, List<DocumentoCargaDTO> documentos);

    // --- REQUERIMIENTOS DE CONDUCTORES ---
    void asociarConductor(Long vehiculoId, Long personaId);
    void actualizarEstadoConductor(Long vehiculoId, Long personaId, String nuevoEstado);

    // --- CONSULTAS Y BÚSQUEDAS ---
    VehiculoDTO buscarPorPlaca(String placa);
    VehiculoDetalleDTO obtenerDetallePorPlaca(String placa);
    List<VehiculoDTO> buscarPorTipoVehiculo(String tipo);
    List<VehiculoDTO> buscarPorEstadoDocumento(String estado);
    List<VehiculoDTO> buscarVehiculosPorTipoDocumento(Long idDocumento);
    List<VehiculoDTO> consultarVehiculosConDocumentosVencidos();
    List<VehiculoDTO> consultarVehiculosPorVencer(int dias);

    // --- AUXILIARES ---
    void agregarDocumentoAVehiculo(Long vehiculoId, Long documentoId, String fechaExp, String fechaVen);
}