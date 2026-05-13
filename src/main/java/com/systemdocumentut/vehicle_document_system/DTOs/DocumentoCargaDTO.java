package com.systemdocumentut.vehicle_document_system.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para el cargue y actualización de documentos de vehículos.
 * Cumple con el requerimiento de recibir un archivo PDF en formato Base64.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoCargaDTO {

    @NotNull(message = "El ID del tipo de documento es obligatorio")
    private Long idDocumento; // Referencia al maestro de documentos (SOAT, etc.)

    @NotBlank(message = "El contenido del documento en Base64 no puede estar vacío")
    private String base64;    // Contenido del PDF en Base64 (Requerimiento BLOB)

    @NotNull(message = "La fecha de expedición es obligatoria")
    @PastOrPresent(message = "La fecha de expedición no puede ser futura")
    private LocalDate fechaExpedicion;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @FutureOrPresent(message = "La fecha de vencimiento debe ser actual o futura")
    private LocalDate fechaVencimiento;
    
    // El estado no se envía desde el cliente, 
    // el sistema lo asigna automáticamente como 'EA' (Espera de Aprobación).
}