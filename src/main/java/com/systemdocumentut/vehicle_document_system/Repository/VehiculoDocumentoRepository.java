package com.systemdocumentut.vehicle_document_system.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.systemdocumentut.vehicle_document_system.Model.VehiculoDocumento;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VehiculoDocumentoRepository extends JpaRepository<VehiculoDocumento, Long> {
    
    List<VehiculoDocumento> findByEstado(String estado);

    void deleteByVehiculoId(Long vehicularId);

    /**
     * Requerimiento Técnico: Tarea programada cada 2 minutos.
     * Busca los documentos vencidos en base al tiempo actual que aún no se han marcado como "VENCIDO".
     */
    @Query("SELECT vd FROM VehiculoDocumento vd WHERE vd.fechaVencimiento < :fechaActual AND vd.estado <> 'VENCIDO'")
    List<VehiculoDocumento> findDocumentosExpirados(@Param("fechaActual") LocalDate fechaActual);
}