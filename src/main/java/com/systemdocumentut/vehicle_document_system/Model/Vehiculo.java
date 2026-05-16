package com.systemdocumentut.vehicle_document_system.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "vehiculos", uniqueConstraints = {@UniqueConstraint(columnNames = "placa")})
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El tipo de vehículo es obligatorio")
    @Pattern(regexp = "^(Automóvil|Motocicleta)$", message = "El tipo debe ser 'Automóvil' o 'Motocicleta'")
    @Column(nullable = false, length = 20)
    private String tipoVehiculo;

    @NotBlank(message = "La placa es obligatoria")
    @Size(min = 6, max = 6, message = "La placa debe tener exactamente 6 caracteres")
    @Column(unique = true, nullable = false, length = 6)
    private String placa;

    @NotBlank(message = "El tipo de servicio es obligatorio")
    @Pattern(regexp = "^(Pu|Pr)$", message = "El servicio debe ser 'Pu' (Público) o 'Pr' (Privado)")
    @Column(nullable = false, length = 2)
    private String tipoServicio;

    @NotBlank(message = "El tipo de combustible es obligatorio")
    @Pattern(regexp = "^(Gasolina|Gas|Disel)$", message = "Combustible debe ser Gasolina, Gas o Disel")
    @Column(nullable = false, length = 20)
    private String tipoCombustible;

    @NotNull(message = "La capacidad de pasajeros es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer capacidadPasajeros;

    @NotBlank(message = "El color es obligatorio")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "El color debe ser un código hexadecimal válido (ej: #FFFFFF)")
    @Column(nullable = false, length = 7)
    private String color;

    @NotNull(message = "El modelo es obligatorio")
    @Min(value = 1900, message = "Modelo no válido")
    @Column(nullable = false)
    private Integer modelo;

    @NotBlank(message = "La marca es obligatoria")
    @Column(nullable = false)
    private String marca;

    @NotBlank(message = "La línea es obligatoria")
    @Column(nullable = false)
    private String linea;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VehiculoConductor> conductoresAsociados;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VehiculoDocumento> documentosAsociados;

    @AssertTrue(message = "El formato de la placa no coincide con el tipo de vehículo")
    public boolean isPlacaValida() {
        if (tipoVehiculo == null || placa == null) return false;
        if (tipoVehiculo.equals("Automóvil")) return placa.matches("^[A-Z]{3}\\d{3}$");
        if (tipoVehiculo.equals("Motocicleta")) return placa.matches("^[A-Z]{3}\\d{2}[A-Z]{1}$");
        return false;
    }
}