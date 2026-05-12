package com.systemdocumentut.vehicle_document_system.Services.impl;

import com.systemdocumentut.vehicle_document_system.Model.Usuario;
import java.util.Optional;

public interface IUsuarioService {
    // Requerimiento: Cambio de contraseña (vía Body)
    void cambiarPassword(String login, String newPassword);

    // Requerimiento: Regenerar APIKey
    String regenerarApiKey(String login);

    // Método necesario para el Middleware de Seguridad
    Optional<Usuario> buscarPorLogin(String login);
}