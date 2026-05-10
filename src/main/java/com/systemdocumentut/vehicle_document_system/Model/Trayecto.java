package com.systemdocumentut.vehicle_document_system.Model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "trayecto")
@Data
public class Trayecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_persona")
    private Persona conductor; // Solo tipo conductor

    @ManyToOne
    @JoinColumn(name = "id_vehiculo")
    private Vehiculo vehiculo;

    private String codigoRuta;

    // Paradas (Coordenadas)
    private Double latInicial;
    private Double lonInicial;
    private Double latFinal;
    private Double lonFinal;
    
    // Puedes usar una lista de Coordenadas para las 5 paradas intermedias
    @OneToMany(cascade = CascadeType.ALL)
    private List<Coordenadas> paradasIntermedias;
}