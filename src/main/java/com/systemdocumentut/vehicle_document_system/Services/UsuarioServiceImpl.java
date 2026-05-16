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

    @Override
    public Optional<Usuario> buscarPorLogin(String login) {
        return usuarioRepo.findById_Login(login);
    }

    @Override
    @Transactional
    public void cambiarPassword(String login, String newPassword) {
        Usuario usuario = usuarioRepo.findById_Login(login)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + login));
        
        usuario.setPassword(newPassword); 
        usuarioRepo.save(usuario);
    }

    @Override
    @Transactional
    public String regenerarApiKey(String login) {
        Usuario usuario = usuarioRepo.findById_Login(login)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + login));
        
        String newKey = UUID.randomUUID().toString();
        usuario.setApikey(newKey);
        usuarioRepo.save(usuario);
        
        return newKey;
    }
}