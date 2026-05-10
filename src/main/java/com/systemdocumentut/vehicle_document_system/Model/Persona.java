package com.systemdocumentut.vehicle_document_system.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "persona")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_persona;

    @NotBlank
    private String identificacion;

    @Pattern(regexp = "CC", message = "Solo se permite CC")
    private String tipoIdentificacion;

    private String nombres;
    private String apellidos;
    private String correoElectronico;

    @Pattern(regexp = "^(C|A)$", message = "C para Conductor, A para Administrativo")
    private String tipoPersona;

    private String ubicacion;
}