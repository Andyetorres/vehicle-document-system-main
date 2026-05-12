package com.systemdocumentut.vehicle_document_system.Repository;

import com.systemdocumentut.vehicle_document_system.Model.VehiculoConductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface VehiculoConductorRepository extends JpaRepository<VehiculoConductor, Long> {
    
    // Para buscar si ya existe la relación
    Optional<VehiculoConductor> findByVehiculoIdAndPersonaId(Long vehiculoId, Long personaId);
    
    // Para los servicios públicos
    List<VehiculoConductor> findByVehiculoId(Long vehiculoId);
}