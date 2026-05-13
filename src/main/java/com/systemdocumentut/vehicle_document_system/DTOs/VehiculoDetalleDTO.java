package com.systemdocumentut.vehicle_document_system.DTOs;

import lombok.Data;
import java.util.List;

@Data
public class VehiculoDetalleDTO {
    private VehiculoDTO vehiculo;
    private List<String> conductores; // Nombres de conductores asociados
    private List<String> documentos;  // Nombres de documentos y sus estados
}