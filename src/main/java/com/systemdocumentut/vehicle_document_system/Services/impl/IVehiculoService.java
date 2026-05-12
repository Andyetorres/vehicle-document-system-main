package com.systemdocumentut.vehicle_document_system.Services.impl;

import com.systemdocumentut.vehicle_document_system.DTOs.VehiculoDTO;
import com.systemdocumentut.vehicle_document_system.DTOs.*;
import com.systemdocumentut.vehicle_document_system.Model.Persona;
import java.util.List;

public interface IVehiculoService {
    // CRUD Básico
    List<VehiculoDTO> listarTodos();
    VehiculoDTO buscarPorId(Long id);
    VehiculoDTO actualizar(Long id, VehiculoDTO dto);
    void eliminar(Long id);

    // Requerimiento Principal: Crear con documento (Estado inicial: "En Verificación")
    VehiculoDTO crearVehiculoConDocumento(VehiculoDTO dto, Long idDocumentoBase);

    // Servicios de Búsqueda específicos
    VehiculoDTO buscarPorPlaca(String placa);
    List<VehiculoDTO> buscarPorTipoVehiculo(String tipo);
    List<VehiculoDTO> buscarPorEstadoDocumento(String estado);
    List<VehiculoDTO> buscarVehiculosPorTipoDocumento(Long idDocumento);
    
    // Servicio para agregar documentos adicionales
    void agregarDocumentoAVehiculo(Long vehiculoId, Long documentoId, String fechaExp, String fechaVen);

    // NUEVOS MÉTODOS (Agrégalos para que desaparezca el error de Override)
    void cargarDocumentosBase64(Long vehiculoId, List<DocumentoCargaDTO> documentos);
    void asociarConductor(Long vehiculoId, Long personaId);
    void actualizarEstadoConductor(Long vehiculoId, Long personaId, String nuevoEstado);
    
    // Métodos para servicios públicos
    VehiculoDetalleDTO obtenerDetallePorPlaca(String placa);
}