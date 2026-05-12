package com.systemdocumentut.vehicle_document_system.Services.impl;

import com.systemdocumentut.vehicle_document_system.Model.VehiculoDocumento;
import java.util.List;

public interface IVehiculoDocumentoService {
    
    VehiculoDocumento asignarDocumento(Long idVehiculo, Long idDocumento, String fechaExp, String fechaVen);
    
    List<VehiculoDocumento> listarPorEstado(String estado);
    
    List<VehiculoDocumento> listarPorVehiculo(Long idVehiculo);

    // Método solicitado para manejo de archivos PDF en Base64
    VehiculoDocumento guardarDocumentoConPdf(Long idVehiculo, Long idDoc, String pdfBase64, String fExp, String fVen);
}