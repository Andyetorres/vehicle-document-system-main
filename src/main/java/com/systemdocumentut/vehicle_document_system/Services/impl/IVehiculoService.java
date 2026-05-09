package com.systemdocumentut.vehicle_document_system.Services.impl;

import com.systemdocumentut.vehicle_document_system.DTOs.VehiculoDTO;
import java.util.List;

public interface IVehiculoService {
    List<VehiculoDTO> listarTodos();
    VehiculoDTO guardar(VehiculoDTO dto);
    // Requerimiento: No crear vehículo sin documento
    VehiculoDTO crearVehiculoConDocumento(VehiculoDTO dto, Long idDocumentoBase);
    VehiculoDTO buscarPorPlaca(String placa);
    VehiculoDTO actualizar(Long id, VehiculoDTO dto); // Para el CRUD (PUT)
    List<VehiculoDTO> buscarPorTipo(String tipo);
    List<VehiculoDTO> buscarPorEstadoDocumento(String estado);
    List<VehiculoDTO> buscarPorTipoDocumento(Long idDocumento); // Requerimiento faltante

    
    
    void eliminar(Long id);
}