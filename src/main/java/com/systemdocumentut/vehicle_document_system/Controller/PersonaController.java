package com.systemdocumentut.vehicle_document_system.Controller;

import com.systemdocumentut.vehicle_document_system.DTOs.PersonaDTO;
import com.systemdocumentut.vehicle_document_system.Model.Persona;
import com.systemdocumentut.vehicle_document_system.Services.PersonaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personas")
@CrossOrigin(origins = "*")
public class PersonaController {

    @Autowired
    private PersonaServiceImpl personaService;

    // REQUERIMIENTO: POST - Crear persona (y usuario si es Administrativo)
    @PostMapping
    public ResponseEntity<?> crearPersona(@RequestBody Persona persona) {
        try {
            Persona nueva = personaService.crearPersona(persona);
            return ResponseEntity.ok(mapToDTO(nueva));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear persona: " + e.getMessage());
        }
    }

    // REQUERIMIENTO: GET - Listar personas
    @GetMapping
    public List<PersonaDTO> listarTodas() {
        return personaService.listarTodas().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // REQUERIMIENTO: PUT - Actualizar persona
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Persona persona) {
        try {
            return ResponseEntity.ok(mapToDTO(personaService.actualizar(id, persona)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Método de apoyo para convertir Entidad a DTO
    private PersonaDTO mapToDTO(Persona p) {
        return PersonaDTO.builder()
                .id(p.getId_persona())
                .identificacion(p.getIdentificacion())
                .tipoIdentificacion(p.getTipoIdentificacion())
                .nombres(p.getNombres())
                .apellidos(p.getApellidos())
                .correoElectronico(p.getCorreoElectronico())
                .tipoPersona(p.getTipoPersona())
                .build();
    }
}