package com.systemdocumentut.vehicle_document_system.Model.Config;

import com.systemdocumentut.vehicle_document_system.Model.Persona;
import com.systemdocumentut.vehicle_document_system.Repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List; // ESTE ES EL IMPORT CORRECTO

@Component
@EnableScheduling
public class GeocodingTask {

    @Autowired
    private PersonaRepository personaRepo;

    // Se ejecuta cada 2 minutos
    @Scheduled(cron = "*/10 * * * * *") //@Scheduled(cron = "0 */2 * * * *")
    public void actualizarUbicaciones() {
        // Ahora List<Persona> funcionará bien
        List<Persona> personas = personaRepo.findAll();
        
        for (Persona p : personas) {
            // Usamos p.getNombres() porque así lo definiste en tu entidad Persona
            System.out.println("Actualizando ubicación de: " + p.getNombres());
            
            // Si necesitas el ID para buscar en la tabla coordenadas:
            Long idActual = p.getId_persona();
            }
    }
}
