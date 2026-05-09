package com.systemdocumentut.vehicle_document_system.Repository;

import com.systemdocumentut.vehicle_document_system.Model.Usuario;
import com.systemdocumentut.vehicle_document_system.Model.UsuarioId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, UsuarioId> {
    
    // Al usar @EmbeddedId, usamos el guion bajo para entrar al objeto ID
    Optional<Usuario> findById_Login(String login);
    
    boolean existsByApikey(String apikey);
}