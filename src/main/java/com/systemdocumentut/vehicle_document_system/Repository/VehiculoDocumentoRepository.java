package com.systemdocumentut.vehicle_document_system.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systemdocumentut.vehicle_document_system.Model.VehiculoDocumento;

import java.util.List;

@Repository
public interface VehiculoDocumentoRepository extends JpaRepository<VehiculoDocumento, Long> {
    
    List<VehiculoDocumento> findByEstado(String estado);

    void deleteByVehiculoId(Long vehiculoId);
}