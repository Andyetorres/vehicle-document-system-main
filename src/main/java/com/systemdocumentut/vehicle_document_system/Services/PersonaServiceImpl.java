package com.systemdocumentut.vehicle_document_system.Services;

import com.systemdocumentut.vehicle_document_system.Model.Persona;
import com.systemdocumentut.vehicle_document_system.Model.Usuario;
import com.systemdocumentut.vehicle_document_system.Model.UsuarioId;
import com.systemdocumentut.vehicle_document_system.Repository.PersonaRepository;
import com.systemdocumentut.vehicle_document_system.Repository.UsuarioRepository;
import com.systemdocumentut.vehicle_document_system.Services.impl.IPersonaService; // Verifica que esta ruta sea correcta
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// IMPORTANTE: Estos son para que 'logger' funcione
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Service
public class PersonaServiceImpl implements IPersonaService {

    // 1. Definimos el logger (esto quita el error de 'logger cannot be resolved')
    private static final Logger logger = LoggerFactory.getLogger(PersonaServiceImpl.class);

    @Autowired 
    private PersonaRepository personaRepo; // Aquí lo llamaste personaRepo

    @Autowired 
    private UsuarioRepository usuarioRepo;

    @Override
    @Transactional
    public Persona crearPersona(Persona persona) {
        Persona nuevaPersona = personaRepo.save(persona);

        if ("A".equalsIgnoreCase(persona.getTipoPersona())) {
            String loginGenerado = persona.getNombres().substring(0,1).toLowerCase() + 
                                   persona.getApellidos().substring(0,1).toLowerCase() + 
                                   persona.getIdentificacion();
            
            UsuarioId idCompuesto = new UsuarioId();
            idCompuesto.setLogin(loginGenerado);
            idCompuesto.setIdpersona(nuevaPersona.getId_persona());

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setId(idCompuesto);
            nuevoUsuario.setPassword("123"); 
            nuevoUsuario.setApikey(UUID.randomUUID().toString());
            
            usuarioRepo.save(nuevoUsuario);
        }
        return nuevaPersona;
    }

    @Override
    public List<Persona> listarTodas() {
        return personaRepo.findAll();
    }

    @Override
    public Persona actualizar(Long id, Persona datosNuevos) {
        Persona p = personaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        
        p.setNombres(datosNuevos.getNombres());
        p.setApellidos(datosNuevos.getApellidos());
        p.setCorreoElectronico(datosNuevos.getCorreoElectronico());
        
        return personaRepo.save(p);
    }

    @Override
    public boolean eliminar(Long id) { 
        try {
            // CORRECCIÓN: Antes decia 'personaRepository', pero tu variable es 'personaRepo'
            if (personaRepo.existsById(id)) {
                personaRepo.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            // Ahora 'logger' ya existe arriba
            logger.error("Error al eliminar: " + e.getMessage());
            return false;
        }
    }
}