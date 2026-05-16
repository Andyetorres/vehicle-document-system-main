package com.systemdocumentut.vehicle_document_system.Services;

import com.systemdocumentut.vehicle_document_system.Model.Persona;
import com.systemdocumentut.vehicle_document_system.Model.Trayecto;
import com.systemdocumentut.vehicle_document_system.Model.VehiculoConductor;
import com.systemdocumentut.vehicle_document_system.Model.VehiculoDocumento;
import com.systemdocumentut.vehicle_document_system.Repository.PersonaRepository;
import com.systemdocumentut.vehicle_document_system.Repository.TrayectoRepository;
import com.systemdocumentut.vehicle_document_system.Repository.VehiculoConductorRepository;
import com.systemdocumentut.vehicle_document_system.Repository.VehiculoDocumentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TareasProgramadasService {

    private static final Logger logger = LoggerFactory.getLogger(TareasProgramadasService.class);

    @Autowired private PersonaRepository personaRepo;
    @Autowired private VehiculoConductorRepository vehiculoConductorRepo;
    @Autowired private VehiculoDocumentoRepository vehiculoDocumentoRepo;
    @Autowired private TrayectoRepository trayectoRepo;

    // TAREA 1: Cada 2 minutos verifica vigencia de licencias de conductores (RO)
    @Scheduled(cron = "0 */2 * * * *")
    @Transactional
    public void verificarVigenciaLicenciasConductores() {
        logger.info("Iniciando verificación cron de licencias de conducción...");
        List<Persona> conductores = personaRepo.findAll().stream()
                .filter(p -> "C".equalsIgnoreCase(p.getTipoPersona()) && p.getFechaVigenciaLicencia() != null)
                .toList();

        for (Persona conductor : conductores) {
            if (conductor.getFechaVigenciaLicencia().isBefore(LocalDate.now())) {
                // Modificar estado en la relación con sus vehículos asociados
                List<VehiculoConductor> relaciones = vehiculoConductorRepo.findAll().stream()
                        .filter(rc -> rc.getPersona().getIdPersona().equals(conductor.getIdPersona()))
                        .toList();

                for (VehiculoConductor relacion : relaciones) {
                    if (!"RO".equalsIgnoreCase(relacion.getEstado())) {
                        relacion.setEstado("RO"); // Restringido para Operar
                        vehiculoConductorRepo.save(relacion);
                        logger.warn("Conductor ID {} cambiado a RO - Restringido para Operar por licencia vencida.", conductor.getIdPersona());
                        
                        // CEREZA: Simulación de envío de correo electrónico informativo
                        enviarCorreoRestriccionSimulado(conductor);
                    }
                }
            }
        }
    }

    // TAREA 2: Cada 2 minutos verifica vigencia de documentos de vehículos
    @Scheduled(cron = "0 */2 * * * *")
    @Transactional
    public void verificarVigenciaDocumentosVehiculos() {
        logger.info("Iniciando verificación cron de vigencia de documentos de vehículos...");
        List<VehiculoDocumento> documentos = vehiculoDocumentoRepo.findAll().stream()
                .filter(vd -> vd.getFechaVencimiento() != null && !"VENCIDO".equalsIgnoreCase(vd.getEstado()))
                .toList();

        for (VehiculoDocumento doc : documentos) {
            if (doc.getFechaVencimiento().isBefore(LocalDate.now())) {
                doc.setEstado("VENCIDO");
                vehiculoDocumentoRepo.save(doc);
                logger.warn("Documento ID {} del Vehículo Placa {} ha sido marcado como VENCIDO.", doc.getId(), doc.getVehiculo().getPlaca());
            }
        }
    }

    // TAREA 3: Cada 90 segundos extrae coordenadas usando API externa de Google Maps
    @Scheduled(fixedRate = 90000)
    @Transactional
    public void complementarCoordenadasGoogleMaps() {
        logger.info("Iniciando sincronización de coordenadas con Google Maps API...");
        List<Trayecto> trayectosSinCoordenadas = trayectoRepo.findAll().stream()
                .filter(t -> t.getLatitud() == null || t.getLongitud() == null)
                .toList();

        // CORRECCIÓN: Se corrigió "trayextosSinCoordenadas" por "trayectosSinCoordenadas"
        for (Trayecto trayecto : trayectosSinCoordenadas) {
            try {
                // Simulación de llamada HTTP al API externa de Google Geocoding utilizando la Ubicación en Texto
                logger.info("Consultando Google Maps para la ubicación: {}", trayecto.getUbicacion());
                
                // Valores mock recuperados de la API externa basados en el ejemplo
                double latitudMock = 4.4389; 
                double longitudMock = -75.2122;

                trayecto.setLatitud(latitudMock);
                trayecto.setLongitud(longitudMock);
                trayectoRepo.save(trayecto);
                
                // CORRECCIÓN: Se cambió trayecto.getIdTrayecto() por trayecto.getId()
                logger.info("Trayecto ID {} actualizado exitosamente con coordenadas de Google Maps.", trayecto.getId());
            } catch (Exception e) {
                // CORRECCIÓN: Se cambió trayecto.getIdTrayecto() por trayecto.getId()
                logger.error("Error consultando la API de Google Maps para el trayecto {}: {}", trayecto.getId(), e.getMessage());
            }
        }
    }

    private void enviarCorreoRestriccionSimulado(Persona conductor) {
        logger.info("CEREZA - [CORREO ENVIADO] Para: {} - Mensaje: Estimado(a) {}, le informamos que sus rutas han sido restringidas debido al vencimiento de su licencia de conducción.", 
                conductor.getCorreoElectronico(), conductor.getNombres());
    }
}