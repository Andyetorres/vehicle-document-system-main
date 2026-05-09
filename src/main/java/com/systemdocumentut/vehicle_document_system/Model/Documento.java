package com.systemdocumentut.vehicle_document_system.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "documentos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    @NotBlank(message = "El nombre del documento es obligatorio")
    private String nombre; // Ejemplo: SOAT

    @NotBlank(message = "Debe indicar a qué aplica (A, M o AM)")
    @Pattern(regexp = "^(A|M|AM)$", message = "Solo se permite A, M o AM")
    private String aplicaA;

    @NotBlank(message = "Debe indicar la obligatoriedad (RA, RM o RR)")
    @Pattern(regexp = "^(RA|RM|RR)$", message = "Solo se permite RA, RM o RR")
    private String obligatorio;

    private String descripcion;
}