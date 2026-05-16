package com.systemdocumentut.vehicle_document_system.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "vehiculo_conductor")
@Data
public class VehiculoConductor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo")
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona")
    private Persona persona;

    @Column(name = "fecha_asociacion")
    private LocalDate fechaAsociacion;
    
    // REQUERIMIENTO: Cambia dinámicamente a "RO" a través de la Tarea Programada si la licencia vence.
    @Column(length = 2, nullable = false)
    private String estado; // PO (Puede Operar), RO (Restringido para Operar), EA (En Espera)
}