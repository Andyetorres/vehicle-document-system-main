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

import java.util.List;
import java.util.UUID;

@Service
public class PersonaServiceImpl implements IPersonaService {

    @Autowired 
    private PersonaRepository personaRepo;

    @Autowired 
    private UsuarioRepository usuarioRepo;

    @Override
    @Transactional
    public Persona crearPersona(Persona persona) {
        // 1. Guardamos la persona primero
        Persona nuevaPersona = personaRepo.save(persona);

        // 2. REQUERIMIENTO: Si es Administrativo ("A"), crear usuario automático
        if ("A".equalsIgnoreCase(persona.getTipoPersona())) {
            
            // Generar login (nemotecnia): inicial nombre + inicial apellido + identificación
            String loginGenerado = persona.getNombres().substring(0,1).toLowerCase() + 
                                   persona.getApellidos().substring(0,1).toLowerCase() + 
                                   persona.getIdentificacion();
            
            // Creamos el objeto de la Clave Compuesta (UsuarioId)
            UsuarioId idCompuesto = new UsuarioId();
            idCompuesto.setLogin(loginGenerado);
            idCompuesto.setIdpersona(nuevaPersona.getId_persona());

            // Creamos el objeto Usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setId(idCompuesto); // Seteamos la clave compuesta
            nuevoUsuario.setPassword("123"); // Password por defecto para pruebas
            nuevoUsuario.setApikey(UUID.randomUUID().toString()); // APIKey aleatoria
            
            // Guardamos el usuario
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
}