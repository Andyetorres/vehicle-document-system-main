package com.systemdocumentut.vehicle_document_system.DTOs;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoDTO {
    private Long id;
    private String tipoVehiculo;
    private String placa;
    private String tipoServicio;
    private String tipoCombustible;
    private Integer capacidadPasajeros;
    private String color;
    private Integer modelo;
    private String marca;
    private String linea;
}