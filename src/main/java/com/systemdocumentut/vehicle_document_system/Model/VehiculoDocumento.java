package com.systemdocumentut.vehicle_document_system.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "vehiculo_documentos")
@Getter @Setter 
@NoArgsConstructor @AllArgsConstructor @Builder
public class VehiculoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id", nullable = false)
    private Documento documento;

    @NotNull(message = "La fecha de expedición es obligatoria")
    @Column(name = "fecha_expedicion")
    private LocalDate fechaExpedicion;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @NotBlank(message = "El estado es obligatorio")
    // Se modifica el Pattern para aceptar "VENCIDO" en mayúsculas exigido por el requerimiento
    @Pattern(regexp = "^(Habilitado|VENCIDO|En Verificación)$", 
             message = "Estado inválido. Use: Habilitado, VENCIDO o En Verificación")
    private String estado; 

    @Lob
    @Column(name = "archivo_pdf", columnDefinition = "LONGTEXT") 
    private String archivoPdf;
}