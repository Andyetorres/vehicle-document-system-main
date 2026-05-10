package com.systemdocumentut.vehicle_document_system.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.systemdocumentut.vehicle_document_system.Model.Coordenadas;

@Repository("ICoordenadasRepository")
public interface CoordenadasRepository extends JpaRepository<Coordenadas, Integer> {

    Page<Coordenadas> findAll(Pageable pageable);
    
    // Ajustado a los nombres de tu Entidad Coordenadas
    @Query("SELECT coord FROM Coordenadas coord WHERE coord.persona = :id_persona")
    Coordenadas getCoordenadaXPersona(@Param("id_persona") int persona);
}