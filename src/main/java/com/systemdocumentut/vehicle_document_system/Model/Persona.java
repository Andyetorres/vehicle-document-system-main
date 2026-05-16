package com.systemdocumentut.vehicle_document_system.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

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
    @Column(name = "tipo_persona", length = 1) // 'C' para Conductor, etc.
    private String tipoPersona;

    // --- REQUERIMIENTO ENTREGA 3: Adición de campos para conductores ---
    @Lob
    @Column(name = "licencia_conduccion", columnDefinition = "LONGBLOB")
    private byte[] licenciaConduccion; // Almacena el archivo BASE64 en formato binario BLOB

    @Column(name = "fecha_vigencia_licencia")
    private LocalDate fechaVigenciaLicencia;

    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL)
    private Usuario usuario;
}