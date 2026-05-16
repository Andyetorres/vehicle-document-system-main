package com.systemdocumentut.vehicle_document_system.Services;

import com.systemdocumentut.vehicle_document_system.Model.Persona;
import com.systemdocumentut.vehicle_document_system.Model.Usuario;
import com.systemdocumentut.vehicle_document_system.Model.UsuarioId;
import com.systemdocumentut.vehicle_document_system.Repository.PersonaRepository;
import com.systemdocumentut.vehicle_document_system.Repository.UsuarioRepository;
import com.systemdocumentut.vehicle_document_system.Services.impl.IPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonaServiceImpl implements IPersonaService {

    private static final Logger logger = LoggerFactory.getLogger(PersonaServiceImpl.class);

    @Autowired 
    private PersonaRepository personaRepo;

    @Autowired 
    private UsuarioRepository usuarioRepo;

    @Override
    @Transactional
    public Persona crearPersona(Persona persona) {
        // Validación previa antes de guardar un conductor
        if ("C".equalsIgnoreCase(persona.getTipoPersona())) {
            if (persona.getLicenciaConduccion() != null && persona.getLicenciaConduccion().length < 10) {
                throw new IllegalArgumentException("La licencia de conducción en bytes no parece ser válida.");
            }
        }
        
        Persona nuevaPersona = personaRepo.save(persona);

        if ("A".equalsIgnoreCase(nuevaPersona.getTipoPersona())) {
            String loginGenerado = (nuevaPersona.getNombres().substring(0, 1) + 
                                   nuevaPersona.getApellidos().substring(0, 1) + 
                                   nuevaPersona.getIdentificacion()).toUpperCase();
            
            UsuarioId idCompuesto = new UsuarioId();
            idCompuesto.setLogin(loginGenerado);
            idCompuesto.setIdPersona(nuevaPersona.getIdPersona()); 

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setId(idCompuesto);
            nuevoUsuario.setPersona(nuevaPersona);
            nuevoUsuario.setPassword(UUID.randomUUID().toString().substring(0, 8)); 
            nuevoUsuario.setApikey(UUID.randomUUID().toString());
            
            usuarioRepo.save(nuevoUsuario);
            logger.info("Usuario creado automáticamente: {}", loginGenerado);
        }
        return nuevaPersona;
    }

    @Override
    public List<Persona> listarTodas() {
        return personaRepo.findAll();
    }

    @Override
    public Optional<Persona> buscarPorId(Long id) {
        return personaRepo.findById(id);
    }

    @Override
    @Transactional
    public Persona actualizar(Long id, Persona datosNuevos) {
        return personaRepo.findById(id)
                .map(p -> {
                    p.setNombres(datosNuevos.getNombres());
                    p.setApellidos(datosNuevos.getApellidos());
                    p.setCorreoElectronico(datosNuevos.getCorreoElectronico());
                    p.setTipoPersona(datosNuevos.getTipoPersona());
                    
                    if ("C".equalsIgnoreCase(datosNuevos.getTipoPersona())) {
                        p.setLicenciaConduccion(datosNuevos.getLicenciaConduccion()); 
                        p.setFechaVigenciaLicencia(datosNuevos.getFechaVigenciaLicencia());
                    }
                    
                    return personaRepo.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + id));
    }

    // =========================================================================
    // IMPLEMENTACIÓN REQUERIMIENTOS ENTREGA 3
    // =========================================================================

    @Override
    @Transactional
    public void actualizarLicenciaConductor(Long personaId, String licenciaBase64, LocalDate fechaVigencia) {
        personaRepo.findById(personaId).ifPresentOrElse(persona -> {
            if (!"C".equalsIgnoreCase(persona.getTipoPersona())) {
                throw new IllegalArgumentException("La persona con ID " + personaId + " no es un conductor.");
            }
            
            try {
                // Limpiar el prefijo data:image/... si viene incluido en el string Base64
                String cleanBase64 = licenciaBase64.contains(",") ? licenciaBase64.split(",")[1] : licenciaBase64;
                
                // Convertir el String Base64 a un arreglo de bytes (byte[]) para tu entidad
                byte[] decodedBytes = Base64.getDecoder().decode(cleanBase64);
                persona.setLicenciaConduccion(decodedBytes);
                
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("La cadena proporcionada no es un Base64 válido.", e);
            }

            persona.setFechaVigenciaLicencia(fechaVigencia);
            personaRepo.save(persona);
            logger.info("Licencia en formato BLOB actualizada para el conductor ID: {}", personaId);
        }, () -> {
            throw new RuntimeException("Conductor no encontrado con ID: " + personaId);
        });
    }

    @Override
    public List<Persona> listarConductoresConLicenciaVencida() {
        // Usamos de forma eficiente la query personalizada de tu PersonaRepository
        return personaRepo.findConductoresConLicenciaVencida(LocalDate.now());
    }

    @Override
    @Transactional
    public void restringirConductorPorLicenciaVencida(Long personaId) {
        personaRepo.findById(personaId).ifPresentOrElse(persona -> {
            if (!"C".equalsIgnoreCase(persona.getTipoPersona())) {
                throw new IllegalArgumentException("La persona con ID " + personaId + " no es un conductor.");
            }

            // NOTA: Como tu entidad 'Persona' no posee una propiedad de estado o de relación con un vehículo aún, 
            // dejamos la lógica sentada en logs. Si en el futuro agregas la entidad 'Vehiculo' o 'ConductorVehiculo',
            // aquí deberás inyectar su respectivo repositorio y cambiar el estado a "RO".
            
            logger.warn("[RESTRICCIÓN] El conductor {} {} (ID: {}) ha sido marcado como 'RO - Restringido para Operar' debido a su licencia vencida.", 
                    persona.getNombres(), persona.getApellidos(), personaId);
                    
        }, () -> {
            throw new RuntimeException("No se encontró el conductor con ID: " + personaId);
        });
    }

    // =========================================================================
    // FIN REQUERIMIENTOS ENTREGA 3
    // =========================================================================

    @Override
    @Transactional
    public void actualizarPassword(String login, String newPassword) {
        Usuario usuario = usuarioRepo.findAll().stream()
                .filter(u -> u.getId().getLogin().equalsIgnoreCase(login))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró el usuario con login: " + login));

        usuario.setPassword(newPassword);
        usuarioRepo.save(usuario);
        logger.info("Contraseña actualizada exitosamente para: {}", login);
    }

    @Override
    @Transactional
    public String regenerarApiKey(String login) {
        Usuario usuario = usuarioRepo.findAll().stream()
                .filter(u -> u.getId().getLogin().equalsIgnoreCase(login))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String nuevaApiKey = UUID.randomUUID().toString();
        usuario.setApikey(nuevaApiKey);
        usuarioRepo.save(usuario);
        return nuevaApiKey;
    }

    @Override
    public Map<String, Long> obtenerEstadisticas() {
        List<Persona> todas = personaRepo.findAll();
        Map<String, Long> stats = new HashMap<>();
        
        stats.put("total", (long) todas.size());
        stats.put("administrativos", todas.stream().filter(p -> "A".equalsIgnoreCase(p.getTipoPersona())).count());
        stats.put("conductores", todas.stream().filter(p -> "C".equalsIgnoreCase(p.getTipoPersona())).count());
        
        return stats;
    }

    @Override
    @Transactional
    public boolean eliminar(Long id) { 
        if (personaRepo.existsById(id)) {
            personaRepo.deleteById(id);
            return true;
        }
        return false;
    }
}