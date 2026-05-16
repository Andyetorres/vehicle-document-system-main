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

    /**
     * CORREGIDO: Se cambia 'Vencido' por 'VENCIDO' para coincidir estricto con la tarea programada.
     */
    @Query("SELECT vd.vehiculo FROM VehiculoDocumento vd WHERE vd.estado = 'VENCIDO'")
    List<Vehiculo> findVehiculosVencidos();

    @Query("SELECT vd.vehiculo FROM VehiculoDocumento vd WHERE vd.fechaVencimiento <= :fechaLimite")
    List<Vehiculo> findByVencimientoProximo(@Param("fechaLimite") LocalDate fechaLimite);

    Optional<Vehiculo> findByPlaca(String placa);

    List<Vehiculo> findByTipoVehiculo(String tipoVehiculo);

    @Query("SELECT vd.vehiculo FROM VehiculoDocumento vd WHERE vd.documento.id = :idDocumento")
    List<Vehiculo> findByDocumentoId(@Param("idDocumento") Long idDocumento);
}