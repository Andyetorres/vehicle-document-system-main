package com.systemdocumentut.vehicle_document_system.Repository;

import com.systemdocumentut.vehicle_document_system.Model.VehiculoConductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface VehiculoConductorRepository extends JpaRepository<VehiculoConductor, Long> {
    
    Optional<VehiculoConductor> findByVehiculoIdAndPersonaId(Long vehiculoId, Long personaId);
    
    List<VehiculoConductor> findByVehiculoId(Long vehiculoId);

    /**
     * Requerimiento Técnico: Permite buscar todas las asignaciones vigentes de un conductor 
     * para cambiar su estado a "RO - Restringido para Operar" de forma masiva si se le vence la licencia.
     */
    List<VehiculoConductor> findByPersonaId(Long personaId);
}