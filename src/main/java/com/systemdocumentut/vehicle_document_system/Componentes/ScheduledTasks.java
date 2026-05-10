package com.systemdocumentut.vehicle_document_system.Componentes;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.systemdocumentut.vehicle_document_system.APIs.Geocoder;
import com.systemdocumentut.vehicle_document_system.Model.Coordenadas;
import com.systemdocumentut.vehicle_document_system.Model.Persona;
import com.systemdocumentut.vehicle_document_system.Repository.CoordenadasRepository;
import com.systemdocumentut.vehicle_document_system.Repository.PersonaRepository;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private PersonaRepository personaRepo;
    
    @Autowired
    private CoordenadasRepository coordenadaRepo;

    // Se ejecuta cada 30 segundos según tu código
   @Scheduled(cron = "*/30 * * * * ?")
public void actualizarCoordenadasDesdeGoogle() {
        try {
            List<Persona> lista = personaRepo.findAll();
            Geocoder geocoder = new Geocoder();

            for (Persona p : lista) {
                // 1. Extraemos la dirección de la base de datos
                String direccion = p.getUbicacion(); 
                
                // 2. Solo actuamos si la persona tiene una dirección escrita
                if (direccion != null && !direccion.isEmpty()) {
                    String latLng = geocoder.getLatLng(direccion);
                    
                    // 3. Verificamos que Google no responda "0,0" (error de API o dirección no encontrada)
                    if (latLng != null && !latLng.equals("0,0")) {
                        String[] coor = latLng.split(",");
                        
                        // Buscamos si esta persona ya tiene un registro en la tabla coordenadas
                        Coordenadas existia = coordenadaRepo.getCoordenadaXPersona(p.getId_persona().intValue());
                        
                        // Construimos el nombre completo para la columna 'me_marca' (como la imagen del profe)
                        String nombreCompleto = p.getNombres() + " " + (p.getApellidos() != null ? p.getApellidos() : "");

                        if (existia == null) {
                            // Si no existe, creamos uno nuevo
                            coordenadaRepo.save(new Coordenadas(
                                null, 
                                p.getId_persona().intValue(), 
                                nombreCompleto, 
                                Double.parseDouble(coor[0]), 
                                Double.parseDouble(coor[1])
                            ));
                        } else {
                            // Si ya existe, actualizamos los datos para no crear duplicados
                            existia.setMe_marca(nombreCompleto);
                            existia.setLatitud(Double.parseDouble(coor[0]));
                            existia.setLongitud(Double.parseDouble(coor[1]));
                            coordenadaRepo.save(existia);
                        }
                        logger.info("Sincronizado con Google Maps: {} -> {}", nombreCompleto, latLng);
                    } else {
                        logger.warn("Google no encontró coordenadas para la dirección: {}", direccion);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error en el ciclo de ScheduledTasks: {}", e.getMessage());
        }
    }
}