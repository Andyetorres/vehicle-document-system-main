package com.systemdocumentut.vehicle_document_system.Controller;

import com.systemdocumentut.vehicle_document_system.Model.Persona;
import com.systemdocumentut.vehicle_document_system.Services.PersonaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador para la gestión de Personas y Usuarios.
 * Integra la seguridad por Token y APIKey a través del JwtFilter.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PersonaController {

    @Autowired
    private PersonaServiceImpl personaService;

    // =========================================================================
    // SERVICIOS PROTEGIDOS 
    // (Requieren Header 'APIKey' y Header 'Authorization: Bearer <token>')
    // El acceso está restringido solo a Administrativos ('A') en el Middleware
    // =========================================================================

    /**
     * REQUERIMIENTO: POST /api/personas
     * Crea una persona. Si es Administrativo ('A'), genera automáticamente 
     * el Usuario con login (nemotecnia), password y APIKey.
     */
    @PostMapping("/personas")
    public ResponseEntity<?> crearPersona(@RequestBody Persona persona) {
        try {
            // Se invoca el método que contiene la lógica de nemotecnia y transaccionalidad
            Persona nuevaPersona = personaService.crearPersona(persona);
            return new ResponseEntity<>(nuevaPersona, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error en el registro: " + e.getMessage());
        }
    }

    /**
     * REQUERIMIENTO: PUT /api/usuarios/{login}/password
     * Actualiza la contraseña de un usuario específico.
     * El login se recibe por URL y la nueva password por el Body.
     */
    @PutMapping("/usuarios/{login}/password")
    public ResponseEntity<?> cambiarPassword(@PathVariable String login, @RequestBody Map<String, String> body) {
        try {
            String newPassword = body.get("password");
            if (newPassword == null || newPassword.isEmpty()) {
                return ResponseEntity.badRequest().body("La nueva contraseña es obligatoria.");
            }
            personaService.actualizarPassword(login, newPassword);
            return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada con éxito para el usuario: " + login));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}