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
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey) {
        
        // 1. Validar presencia de X-API-KEY en Header (Requerimiento de Seguridad)
        if (apiKey == null || apiKey.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: El encabezado 'X-API-KEY' es obligatorio para la autenticación.");
        }

        // 2. Buscar usuario por login (usando la estructura de ID compuesto)
        return usuarioRepo.findById_Login(request.getUsername())
            .map(user -> {
                
                // 3. Validar que la persona asociada sea ADMINISTRADOR ('A')
                // Solo los administrativos pueden acceder al sistema según la regla de negocio.
                if (!"A".equals(user.getPersona().getTipoPersona())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("Acceso denegado: Solo usuarios administrativos pueden iniciar sesión.");
                }

                // 4. Validar Password (en texto plano según tu lógica actual) y APIKey
                if (user.getPassword().equals(request.getPassword()) && 
                    user.getApikey().equals(apiKey)) {
                    
                    // 5. Generar Token JWT incluyendo el login como subject
                    String token = jwtAuthtenticationConfig.getJWTToken(user.getId().getLogin());
                    
                    return ResponseEntity.ok(new JwtResponse(token));
                }
                
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Credenciales incorrectas: Password o APIKey no coinciden.");
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El login '" + request.getUsername() + "' no existe en el sistema."));
    }
}