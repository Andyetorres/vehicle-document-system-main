package com.systemdocumentut.vehicle_document_system.Componentes;

import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.systemdocumentut.vehicle_document_system.APIs.Geocoder;
import com.systemdocumentut.vehicle_document_system.Model.Trayecto;
import com.systemdocumentut.vehicle_document_system.Model.Persona;
import com.systemdocumentut.vehicle_document_system.Repository.TrayectoRepository;
import com.systemdocumentut.vehicle_document_system.Repository.PersonaRepository;
// Importa tus repositorios de relaciones de estado aquí
// import com.systemdocumentut.vehicle_document_system.Repository.ConductorVehiculoRepository;
// import com.systemdocumentut.vehicle_document_system.Repository.DocumentoVehiculoRepository;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private TrayectoRepository trayectoRepo;

    @Autowired
    private PersonaRepository personaRepo;

    @Autowired
    private Geocoder geocoder; // Inyectado idealmente como Bean/Componente

    /**
     * TAREA 1: Cada 2 minutos verifica vigencia de licencias de conductores.
     * Si están vencidas, cambia el estado a "RO - Restringido para Operar".
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void verificarVigenciaLicencias() {
        logger.info("Iniciando verificación programada de licencias de conducción...");
        try {
            List<Persona> conductores = personaRepo.findAll(); // O filtrar directamente por tipo "C" en repositorio
            LocalDate hoy = LocalDate.now();

            for (Persona conductor : conductores) {
                if ("C".equals(conductor.getTipoPersona()) && conductor.getFechaVigenciaLicencia() != null) {
                    if (conductor.getFechaVigenciaLicencia().isBefore(hoy)) {
                        
                        // TODO: Aquí ejecutas la lógica o llamada al repositorio de la relación 
                        // Conductor-Vehículo para cambiar el estado a "RO - Restringido para Operar".
                        
                        logger.warn("Conductor ID {} tiene la licencia vencida. Estado cambiado a RO.", conductor.getIdPersona());
                        
                        // CEREZA (Opcional): Aquí se gatillaría el envío de correo electrónico[cite: 34].
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error al verificar vigencia de licencias: {}", e.getMessage());
        }
    }

    /**
     * TAREA 2: Cada 2 minutos verifica vigencia de documentos de vehículos.
     * Si están vencidos, cambia el estado del documento a "VENCIDO".
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void verificarDocumentosVehiculos() {
        logger.info("Iniciando verificación programada de documentos de vehículos...");
        try {
            // TODO: Buscar documentos de vehículos desde tu repositorio correspondiente
            // y comparar la fecha de vigencia con LocalDate.now().
            // Si está vencido -> documento.setEstado("VENCIDO"); 
        } catch (Exception e) {
            logger.error("Error al verificar documentos de vehículos: {}", e.getMessage());
        }
    }

    /**
     * TAREA 3: Cada 90 segundos verifica los trayectos sin coordenadas asociadas,
     * consulta la API de Google Maps y los persiste en la Base de Datos.
     */
    @Scheduled(fixedRate = 90000) // 90 segundos = 90000 milisegundos 
    public void actualizarCoordenadasTrayectos() {
        logger.info("Iniciando sincronización de coordenadas para trayectos pendientes...");
        try {
            // Buscamos únicamente trayectos que no tengan latitud o longitud asignada 
            List<Trayecto> trayectosPendientes = trayectoRepo.findTrayectosSinCoordenadas();

            for (Trayecto trayecto : trayectosPendientes) {
                String direccion = trayecto.getUbicacion(); // [cite: 22]

                if (direccion != null && !direccion.isEmpty()) {
                    String latLng = geocoder.getLatLng(direccion); // Consulta a Google Maps 

                    if (latLng != null && !latLng.equals("0,0")) {
                        String[] coor = latLng.split(",");
                        
                        // Seteamos los valores directamente en la entidad Trayecto 
                        trayecto.setLatitud(Double.parseDouble(coor[0])); // [cite: 24]
                        trayecto.setLongitud(Double.parseDouble(coor[1])); // [cite: 24]
                        
                        // Guardamos/actualizamos el mismo trayecto en la base de datos 
                        trayectoRepo.save(trayecto);
                        
                        logger.info("Trayecto ID {} actualizado con éxito. Ubicación: {} -> Coordenadas: {}", 
                        trayecto.getId(), direccion, latLng);
                    } else {
                        logger.warn("No se lograron extraer coordenadas válidas de Google Maps para el trayecto ID: {}", 
                        trayecto.getId());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error en la tarea de geolocalización de trayectos: {}", e.getMessage());
        }
    }
}