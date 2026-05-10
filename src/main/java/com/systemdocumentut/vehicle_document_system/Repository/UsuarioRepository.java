package com.systemdocumentut.vehicle_document_system.Repository;

import com.systemdocumentut.vehicle_document_system.Model.Usuario;
import com.systemdocumentut.vehicle_document_system.Model.UsuarioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository("IUsuarioRepository")
public interface UsuarioRepository extends JpaRepository<Usuario, UsuarioId> {

    // 1. Mantenemos el que ya funcionaba para tu Login JWT
    Optional<Usuario> findById_Login(String login);

    // 2. Corregimos el método del profe usando @Query para que no de error de "No property found"
    @Query("SELECT u FROM Usuario u WHERE u.id.login = :login")
    Usuario findByUsername(@Param("login") String login);

    // IMPORTANTE: Aquí es donde estaba el error. Usamos JPQL para mapear a tus campos reales
    boolean existsByApikey(String apikey);
        
    @Query("SELECT u FROM Usuario u WHERE u.id.login = :login AND u.apikey = :apikey")
    Usuario findByUsernameANDAPIKey(@Param("login") String login, @Param("apikey") String apikey);
}
