package com.systemdocumentut.vehicle_document_system.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoDTO {
    private Long id;

    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Debe especificar a qué aplica (ej. Moto, Carro, Todos)")
    private String aplicaA;

    @NotBlank(message = "Especifique si es obligatorio (SI/NO)")
    private String obligatorio;

    private String descripcion;
}