package com.systemdocumentut.vehicle_document_system.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Entidad que representa un Vehículo.
 * Basado en los requerimientos de la Entrega 1.
 */
@Entity
@Table(name = "vehiculos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador de vehículo (Primary Key)

    @NotBlank(message = "El tipo de vehículo es obligatorio (Automóvil - Motocicleta)")
    @Column(nullable = false, length = 20)
    private String tipoVehiculo; // Automóvil o Motocicleta

    @NotBlank(message = "La placa es obligatoria")
    @Column(unique = true, nullable = false, length = 6) // Debe ser único
    private String placa; // Longitud exacta de 6 caracteres

    @NotBlank(message = "El tipo de servicio es obligatorio (Pu o Pr)")
    @Column(nullable = false, length = 10)
    private String tipoServicio; // Público (Pu) o Privado (Pr)

    @NotBlank(message = "El tipo de combustible es obligatorio")
    @Column(nullable = false, length = 20)
    private String tipoCombustible; // Gasolina - Gas - Disel

    @NotNull(message = "La capacidad de pasajeros es obligatoria")
    @Column(nullable = false)
    private Integer capacidadPasajeros; // Numérico y de tipo entero

    @NotBlank(message = "El color es obligatorio")
    @Column(nullable = false, length = 7)
    private String color; // Representación en código hexadecimal

    @NotNull(message = "El modelo es obligatorio")
    @Column(nullable = false)
    private Integer modelo; // Numérico y de tipo entero

    @NotBlank(message = "La marca es obligatoria")
    private String marca; // Ejemplo: Toyota

    @NotBlank(message = "La línea es obligatoria")
    private String linea; // Ejemplo: Fortuner SW
}