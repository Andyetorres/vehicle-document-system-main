package com.systemdocumentut.vehicle_document_system.Services.impl;

import com.systemdocumentut.vehicle_document_system.Model.VehiculoDocumento;
import java.util.List;

public interface IVehiculoDocumentoService {
    
    VehiculoDocumento asignarDocumento(Long idVehiculo, Long idDocumento, String fechaExp, String fechaVen);
    
    List<VehiculoDocumento> listarPorEstado(String estado);
    
    List<VehiculoDocumento> listarPorVehiculo(Long idVehiculo);

    VehiculoDocumento guardarDocumentoConPdf(Long idVehiculo, Long idDoc, String pdfBase64, String fExp, String fVen);

    // --- NUEVOS REQUERIMIENTOS: TAREAS PROGRAMADAS (ENTREGA 3) ---

    /**
     * Tarea programada (2 min): Busca todos los registros de documentos asociados a vehículos vencidos.
     */
    List<VehiculoDocumento> listarDocumentosVehiculoExpirados();

    /**
     * Tarea programada (2 min): Cambia el estado del documento asociado al vehículo a "VENCIDO".
     */
    void marcarDocumentoComoVencido(Long vehiculoDocumentoId);
}