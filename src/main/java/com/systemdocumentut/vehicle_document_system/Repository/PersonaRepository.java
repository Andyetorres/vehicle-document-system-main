package com.systemdocumentut.vehicle_document_system.Repository;

import com.systemdocumentut.vehicle_document_system.Model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository("IPersonaRepository") 
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    
    List<Persona> findByNombres(String nombres);

    Optional<Persona> findById(Long id);
    
    @Query("SELECT per FROM Persona per")
    List<Persona> getPersonas();

    /**
     * Requerimiento Técnico: Tarea programada cada 2 minutos.
     * Busca las personas de tipo Conductor ('C') cuya licencia esté vencida respecto a la fecha actual.
     */
    @Query("SELECT p FROM Persona p WHERE p.tipoPersona = 'C' AND p.fechaVigenciaLicencia < :fechaActual")
    List<Persona> findConductoresConLicenciaVencida(@Param("fechaActual") LocalDate fechaActual);
}