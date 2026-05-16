package com.systemdocumentut.vehicle_document_system.Model.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Component
@EnableScheduling
public class CronTasksConfig {

    // Asumiendo la inyección de tus repositorios o servicios de negocio
    // @Autowired 
    // private ConductorVehiculoRepository conductorVehiculoRepo;
    // @Autowired 
    // private DocumentoVehiculoRepository documentoVehiculoRepo;
    // @Autowired 
    // private TrayectoRepository trayectoRepository;

    /**
     * TAREA 1: Cada 2 minutos (120000 ms) verifica licencias vencidas.
     * Cambia el estado a 'RO - Restringido para Operar'.
     */
    @Scheduled(fixedRate = 120000)
    @Transactional
    public void verificarVigenciaLicencias() {
        System.out.println("Cron ejecutado: Verificando vigencia de licencias de conducción...");
        
        Date fechaActual = new Date();
        
        // Aquí debes ejecutar la lógica de actualización en tu BD. Ejemplo conceptual:
        // conductorVehiculoRepo.restringirConductoresConLicenciaVencida(fechaActual);
        
        // CEREZA (Opcional): Lógica para enviar correo electrónico [cite: 34]
        // emailService.enviarCorreoRestriccion(...);
    }

    /**
     * TAREA 2: Cada 2 minutos (120000 ms) verifica documentos de vehículos vencidos.
     * Cambia el estado del documento a 'VENCIDO'.
     */
    @Scheduled(fixedRate = 120000)
    @Transactional
    public void verificarVigenciaDocumentosVehiculo() {
        System.out.println("Cron ejecutado: Verificando documentos de vehículos vencidos...");
        
        Date fechaActual = new Date();
        
        // Aquí ejecutas la actualización en base de datos. Ejemplo conceptual:
        // documentoVehiculoRepo.marcarDocumentosVencidos(fechaActual);
    }

    /**
     * TAREA 3: Cada 90 segundos (90000 ms) extrae coordenadas de Google Maps
     * para trayectos que carecen de latitud y longitud.
     */
    @Scheduled(fixedRate = 90000)
    @Transactional
    public void complementarCoordenadasTrayectos() {
        System.out.println("Cron ejecutado: Buscando trayectos sin coordenadas geográficas...");
        
        // 1. Obtener de la base de datos trayectos donde latitud o longitud sean nulas
        // List<Trayecto> trayectosIncompletos = trayectoRepository.findByLatitudIsNullOrLongitudIsNull();
        
        // 2. Recorrer y consumir la API Externa de Google Maps Geocoding usando la 'ubicación' [cite: 22, 36]
        /*
        for (Trayecto t : trayentrysIncompletos) {
            String direccion = t.getUbicacion();
            // Coordenadas coord = googleMapsClient.obtenerCoordenadas(direccion);
            // t.setLatitud(coord.getLat());
            // t.setLongitud(coord.getLng());
            // trayectoRepository.save(t);
        }
        */
    }
}