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

    @ManyToOne
    @JoinColumn(name = "id_vehiculo")
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "id_persona")
    private Persona persona;

    private LocalDate fechaAsociacion;
    
    // REQUERIMIENTO: Estados PO, EA, RO
    private String estado; 
}