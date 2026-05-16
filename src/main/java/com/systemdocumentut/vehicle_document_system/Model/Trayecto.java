package com.systemdocumentut.vehicle_document_system.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "trayecto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trayecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador de trayecto (Primary Key)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona conductor; // Debe validarse en lógica que sea tipo conductor 'C'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @NotBlank
    @Column(name = "codigo_ruta", nullable = false)
    private String codigoRuta; // Código que agrupa varios trayectos/paradas

    @NotBlank
    @Column(name = "ubicacion", nullable = false)
    private String ubicacion; // Lugar de parada (ej: Conservatorio del Tolima)

    @NotNull
    @Column(name = "orden_parada", nullable = false)
    private Integer ordenParada; // 0 = Inicial, mayor = Final, intermedios

    // Coordenadas geográficas (pueden ser NULL inicialmente para la tarea programada de Google Maps)
    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @NotBlank
    @Column(name = "login_usuario_registro", nullable = false)
    private String loginUsuarioRegistro; // Login del usuario que registra el trayecto
}