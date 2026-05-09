package com.systemdocumentut.vehicle_document_system.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.systemdocumentut.vehicle_document_system.Model.Config.JwtRequest;
import com.systemdocumentut.vehicle_document_system.Model.Config.JwtResponse;
import com.systemdocumentut.vehicle_document_system.Model.Config.JWTAuthtenticationConfig;
import com.systemdocumentut.vehicle_document_system.Repository.UsuarioRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class JWTAuthenticationController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private JWTAuthtenticationConfig jwtAuthtenticationConfig;

    @PostMapping("/authenticate")
    public ResponseEntity<?> login(
            @RequestBody JwtRequest request, 
            @RequestHeader(value = "APIKey", required = false) String apiKey) {
        
        // 1. Validar presencia de APIKey en Header
        if (apiKey == null || apiKey.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: El encabezado 'APIKey' es obligatorio.");
        }

        // 2. Buscar por login usando la convención de @EmbeddedId
        return usuarioRepo.findById_Login(request.getUsername())
            .map(user -> {
                // 3. Validar Password y APIKey contra la DB
                if (user.getPassword().equals(request.getPassword()) && 
                    user.getApikey().equals(apiKey)) {
                    
                    // 4. Generar Token con la nueva configuración de seguridad
                    String token = jwtAuthtenticationConfig.getJWTToken(request.getUsername());
                    return ResponseEntity.ok(new JwtResponse(token));
                }
                
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Credenciales incorrectas: Password o APIKey no coinciden.");
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario '" + request.getUsername() + "' no encontrado."));
    }
}