package com.systemdocumentut.vehicle_document_system.DTOs;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoDocumentoDTO {
    private Long id;
    private Long vehiculoId;  // Solo el ID para evitar cargar todo el objeto
    private Long documentoId; 
    private String nombreDocumento; // Para mostrarlo en el JSON
    private LocalDate fechaExpedicion;
    private LocalDate fechaVencimiento;
    private String estado;
}