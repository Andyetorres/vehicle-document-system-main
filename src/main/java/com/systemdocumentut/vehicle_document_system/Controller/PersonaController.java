package com.systemdocumentut.vehicle_document_system.Controller;

import com.systemdocumentut.vehicle_document_system.DTOs.PersonaDTO;
import com.systemdocumentut.vehicle_document_system.Model.Persona;
import com.systemdocumentut.vehicle_document_system.Services.PersonaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PersonaController {

    @Autowired
    private PersonaServiceImpl personaService;

    // --- SERVICIOS PROTEGIDOS (Requieren Token + APIKey en el Middleware) ---

    /**
     * POST /api/personas
     * Crea persona y si es tipo 'A', crea automáticamente el Usuario con nemotecnia.
     */
    @PostMapping("/personas")
    public ResponseEntity<?> crearPersona(@RequestBody Persona persona) {
        try {
            Persona nuevaPersona = personaService.crearPersonaConUsuario(persona);
            return ResponseEntity.ok(nuevaPersona);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear: " + e.getMessage());
        }
    }

    /**
     * GET /api/personas/{id}
     * Obtiene datos de una persona específica.
     */
    @GetMapping("/personas/{id}")
    public ResponseEntity<?> obtenerPersona(@PathVariable Long id) {
        return personaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/usuarios/{login}/password
     * Cambio de contraseña vía Body.
     */
    @PutMapping("/usuarios/{login}/password")
    public ResponseEntity<?> cambiarPassword(@PathVariable String login, @RequestBody Map<String, String> body) {
        try {
            String newPassword = body.get("password");
            personaService.actualizarPassword(login, newPassword);
            return ResponseEntity.ok("Contraseña actualizada con éxito.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET /api/usuarios/{login}/new-apikey
     * Regenera la APIKey de un usuario.
     */
    @GetMapping("/usuarios/{login}/new-apikey")
    public ResponseEntity<?> regenerarApiKey(@PathVariable String login) {
        try {
            String newKey = personaService.regenerarApiKey(login);
            return ResponseEntity.ok(Map.of("apiKey", newKey));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- SERVICIOS PÚBLICOS (Sin Token) ---

    /**
     * GET /api/public/personas/estadisticas
     * SELECT tipo_persona, COUNT(*) FROM Persona GROUP BY tipo_persona
     */
    @GetMapping("/public/personas/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        return ResponseEntity.ok(personaService.obtenerEstadisticas());
    }
}