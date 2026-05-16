package com.systemdocumentut.vehicle_document_system.Services.impl;

import com.systemdocumentut.vehicle_document_system.Model.Usuario;
import java.util.Optional;

public interface IUsuarioService {
    void cambiarPassword(String login, String newPassword);
    String regenerarApiKey(String login);
    Optional<Usuario> buscarPorLogin(String login);
}