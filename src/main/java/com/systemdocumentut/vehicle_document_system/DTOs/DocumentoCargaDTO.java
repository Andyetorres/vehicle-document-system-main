package com.systemdocumentut.vehicle_document_system.DTOs;

import java.time.LocalDate;
import lombok.Data;

@Data
public class DocumentoCargaDTO {
    private Long idDocumento; // ID del tipo de documento (SOAT, Tecno, etc)
    private String base64;    // El contenido del PDF
    private LocalDate fechaExpedicion;
    private LocalDate fechaVencimiento;
}