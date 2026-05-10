package com.systemdocumentut.vehicle_document_system.Controller;

import com.systemdocumentut.vehicle_document_system.DTOs.PersonaDTO;
import com.systemdocumentut.vehicle_document_system.Model.Persona;
import com.systemdocumentut.vehicle_document_system.Repository.PersonaRepository;
import com.systemdocumentut.vehicle_document_system.Services.PersonaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/LaboratorioV1")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})public class PersonaController {

    @Autowired
    private PersonaRepository personaRepo;

    @Autowired
    private PersonaServiceImpl personaService;

        @PutMapping("/{id}")
        public ResponseEntity<?> actualizarPersona(@PathVariable Long id, @RequestBody Persona personaDetalles) {
            try {
                // Ahora 'id' es Long, así que findById(id) funcionará sin quejas
                Persona persona = personaRepo.findById(id)
                    .orElseThrow(() -> new Exception("Persona no encontrada con id: " + id));

            // 2. Actualizamos los campos necesarios
            persona.setNombres(personaDetalles.getNombres());
            persona.setApellidos(personaDetalles.getApellidos());
            persona.setUbicacion(personaDetalles.getUbicacion()); // <--- ESTO ES LO IMPORTANTE
            persona.setCorreoElectronico(personaDetalles.getCorreoElectronico());

            // 3. Guardamos los cambios
            personaRepo.save(persona);
            
            return ResponseEntity.ok("Persona actualizada correctamente. El Cron detectará el cambio en unos segundos.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // REQUERIMIENTO: GET - Listar personas (Imagen 1 del profe)
    @GetMapping("/personas")
    public List<PersonaDTO> listarTodas() {
        return personaService.listarTodas().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Actualiza el método mapToDTO para que incluya la UBICACION
    private PersonaDTO mapToDTO(Persona p) {
        return PersonaDTO.builder()
                .id(p.getId_persona())
                .identificacion(p.getIdentificacion())
                .tipoIdentificacion(p.getTipoIdentificacion())
                .nombres(p.getNombres())
                .apellidos(p.getApellidos())
                .correoElectronico(p.getCorreoElectronico())
                .tipoPersona(p.getTipoPersona())
                .ubicacion(p.getUbicacion()) // <--- IMPORTANTE: Agrega esto a tu DTO
                .build();
    }

    
}