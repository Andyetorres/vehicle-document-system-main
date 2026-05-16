package com.systemdocumentut.vehicle_document_system.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

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

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    @Column(nullable = false)
    @NotBlank(message = "El nombre del documento es obligatorio")
    private String nombre;

    @Column(length = 2, nullable = false)
    @NotBlank(message = "Debe indicar a qué aplica (A, M o AM)")
    @Pattern(regexp = "^(A|M|AM)$", message = "Valores permitidos: A (Automóvil), M (Moto), AM (Ambos)")
    private String aplicaA;

    @Column(length = 2, nullable = false)
    @NotBlank(message = "Debe indicar la obligatoriedad (RA, RM o RR)")
    @Pattern(regexp = "^(RA|RM|RR)$", message = "Valores permitidos: RA (Req. Auto), RM (Req. Moto), RR (Req. Ambos)")
    private String obligatorio;

    private String descripcion;

    @OneToMany(mappedBy = "documento", cascade = CascadeType.ALL)
    private List<VehiculoDocumento> vehiculosAsociados;
}