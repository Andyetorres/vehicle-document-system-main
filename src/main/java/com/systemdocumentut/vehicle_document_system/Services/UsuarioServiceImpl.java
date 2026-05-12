package com.systemdocumentut.vehicle_document_system.Services;

import com.systemdocumentut.vehicle_document_system.Model.Usuario;
import com.systemdocumentut.vehicle_document_system.Repository.UsuarioRepository;
import com.systemdocumentut.vehicle_document_system.Services.impl.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired 
    private UsuarioRepository usuarioRepo;

    /**
     * SOLUCIÓN AL ERROR: Implementación del método faltante.
     * Este método es crucial para validar el login en el Middleware.
     */
    @Override
    public Optional<Usuario> buscarPorLogin(String login) {
        // Accedemos al repositorio usando la propiedad del ID compuesto
        return usuarioRepo.findById_Login(login);
    }

    // REQUERIMIENTO: PUT /api/usuarios/{login}/password
    @Override
    @Transactional
    public void cambiarPassword(String login, String newPassword) {
        Usuario usuario = usuarioRepo.findById_Login(login)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + login));
        
        // El requerimiento pide recibir la nueva contraseña por el body
        usuario.setPassword(newPassword); 
        usuarioRepo.save(usuario);
    }

    // REQUERIMIENTO: GET /api/usuarios/{login}/new-apikey
    @Override
    @Transactional
    public String regenerarApiKey(String login) {
        Usuario usuario = usuarioRepo.findById_Login(login)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + login));
        
        // Generación automática de valor único
        String newKey = UUID.randomUUID().toString();
        usuario.setApikey(newKey);
        usuarioRepo.save(usuario);
        
        return newKey;
    }
}