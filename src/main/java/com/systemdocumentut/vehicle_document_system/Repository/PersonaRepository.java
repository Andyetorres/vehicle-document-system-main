package com.systemdocumentut.vehicle_document_system.Repository;

import com.systemdocumentut.vehicle_document_system.Model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("IPersonaRepository") 
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    
    // Cambiamos findByPnombre por findByNombres
    List<Persona> findByNombres(String nombres);

    // Cambiamos el tipo de retorno de findById para que use Long (como tu entidad)
    Optional<Persona> findById(Long id);
    
    @Query("SELECT per FROM Persona per") // Ajustado a tu entidad
    List<Persona> getPersonas();
}