package com.systemdocumentut.vehicle_document_system.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.systemdocumentut.vehicle_document_system.Model.Config.JwtRequest;
import com.systemdocumentut.vehicle_document_system.Model.Config.JwtResponse;
import com.systemdocumentut.vehicle_document_system.Model.Config.JWTAuthtenticationConfig;
import com.systemdocumentut.vehicle_document_system.Repository.UsuarioRepository;
import com.systemdocumentut.vehicle_document_system.Model.Usuario;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class JWTAuthenticationController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private JWTAuthtenticationConfig jwtAuthtenticationConfig;

    /**
     * POST /auth/authenticate
     * Servicio de login que valida credenciales y APIKey.
     * Requerimiento: Solo usuarios Administrativos pueden acceder.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> login(
            @RequestBody JwtRequest request, 
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey) {
        
        // 1. Validar presencia de X-API-KEY en Header (Requerimiento de Seguridad)
        if (apiKey == null || apiKey.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Seguridad", "mensaje", "El encabezado 'X-API-KEY' es obligatorio."));
        }

        // 2. Buscar usuario por login (Buscando en el campo login del ID compuesto)
        // Se asume que en el repositorio existe el método findById_Login
        return usuarioRepo.findById_Login(request.getUsername())
            .map(user -> {
                
                // 3. REQUERIMIENTO DE NEGOCIO: Validar que la persona sea ADMINISTRATIVO ('A')
                // Solo los administrativos tienen usuario según las reglas de la base de datos.
                if (!"A".equalsIgnoreCase(user.getPersona().getTipoPersona())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("error", "Acceso denegado", "mensaje", "Solo el personal administrativo puede acceder."));
                }

                // 4. Validar Password y APIKey (Doble factor de seguridad)
                // Se compara el APIKey enviado en la cabecera contra el almacenado en la BD
                if (user.getPassword().equals(request.getPassword()) && 
                    user.getApikey().equals(apiKey)) {
                    
                    // 5. Generar Token JWT incluyendo el login como subject
                    String token = jwtAuthtenticationConfig.getJWTToken(user.getId().getLogin());
                    
                    return ResponseEntity.ok(new JwtResponse(token));
                }
                
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "No autorizado", "mensaje", "Credenciales o APIKey incorrectos."));
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No encontrado", "mensaje", "El usuario '" + request.getUsername() + "' no existe.")));
    }
}