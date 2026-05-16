package com.systemdocumentut.vehicle_document_system.Controller;

import com.systemdocumentut.vehicle_document_system.Model.Persona;
import com.systemdocumentut.vehicle_document_system.Services.PersonaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PersonaController {

    @Autowired
    private PersonaServiceImpl personaService;

    @PostMapping("/personas")
    public ResponseEntity<?> crearPersona(@RequestBody Persona persona) {
        try {
            Persona nuevaPersona = personaService.crearPersona(persona);
            return new ResponseEntity<>(nuevaPersona, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error en validación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el registro: " + e.getMessage());
        }
    }

    @PutMapping("/usuarios/{login}/password")
    public ResponseEntity<?> cambiarPassword(@PathVariable String login, @RequestBody Map<String, String> body) {
        try {
            String newPassword = body.get("password");
            if (newPassword == null || newPassword.isEmpty()) {
                return ResponseEntity.badRequest().body("La nueva contraseña es obligatoria.");
            }
            personaService.actualizarPassword(login, newPassword);
            return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada con éxito para el usuario: " + login));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cambiar contraseña.");
        }
    }

    /**
     * REQUERIMIENTO: Adición de campos entidad Persona (Licencia de Conducción BLOB/Base64 y Fecha de Vigencia)
     * Aplica cuando la persona es tipo conductor 'C'.
     */
    @PatchMapping("/personas/{id}/licencia")
    public ResponseEntity<?> actualizarLicencia(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> payload) {
        try {
            if (!payload.containsKey("licencia") || !payload.containsKey("fechaVigencia")) {
                return ResponseEntity.badRequest().body("Los campos 'licencia' y 'fechaVigencia' son requeridos.");
            }
            
            String licenciaBase64 = payload.get("licencia").toString();
            String fechaVigenciaStr = payload.get("fechaVigencia").toString();
            
            // CORRECCIÓN: Parsear el String a LocalDate (Format: YYYY-MM-DD)
            LocalDate fechaVigencia = LocalDate.parse(fechaVigenciaStr);
            
            personaService.actualizarLicenciaConductor(id, licenciaBase64, fechaVigencia);
            return ResponseEntity.ok(Map.of("mensaje", "Licencia de conducción y fecha de vigencia registradas con éxito."));
            
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Formato de fecha inválido. Use el formato 'YYYY-MM-DD'.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar licencia: " + e.getMessage());
        }
    }
}