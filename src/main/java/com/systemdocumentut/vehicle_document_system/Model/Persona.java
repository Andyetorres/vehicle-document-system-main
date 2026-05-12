package com.systemdocumentut.vehicle_document_system.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "persona")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long idPersona;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String identificacion;

    @NotBlank
    @Column(name = "tipo_identificacion", length = 2)
    // Nota: El CHECK se define en el script SQL o vía columnDefinition
    private String tipoIdentificacion;

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @Email
    @NotBlank
    @Column(unique = true, name = "correo_electronico")
    private String correoElectronico;

    @NotBlank
    @Column(name = "tipo_persona", length = 1)
    private String tipoPersona;

    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL)
    private Usuario usuario;
}