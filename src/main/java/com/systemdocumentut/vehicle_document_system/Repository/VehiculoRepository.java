package com.systemdocumentut.vehicle_document_system.Repository;

import com.systemdocumentut.vehicle_document_system.Model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    // Consultar vehículos con documentos vencidos
    @Query("SELECT vd.vehiculo FROM VehiculoDocumento vd WHERE vd.estado = 'Vencido'")
    List<Vehiculo> findVehiculosVencidos();

    // Consultar vehículos con documentos por vencer (parámetro de fecha)
    @Query("SELECT vd.vehiculo FROM VehiculoDocumento vd WHERE vd.fechaVencimiento <= :fechaLimite")
    List<Vehiculo> findByVencimientoProximo(@Param("fechaLimite") LocalDate fechaLimite);
    /**
     * Requerimiento: Buscar vehículo por número de placa.
     * @param placa Número de placa único.
     * @return El vehículo si existe.
     */
    Optional<Vehiculo> findByPlaca(String placa);

    /**
     * Requerimiento: Buscar vehículos por tipo de vehículo (Automóvil/Motocicleta).
     * @param tipoVehiculo Categoría del vehículo.
     * @return Lista de vehículos que coinciden.
     */
    List<Vehiculo> findByTipoVehiculo(String tipoVehiculo);

    @Query("SELECT vd.vehiculo FROM VehiculoDocumento vd WHERE vd.documento.id = :idDocumento")
    List<Vehiculo> findByDocumentoId(@Param("idDocumento") Long idDocumento);
}