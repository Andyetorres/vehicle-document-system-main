package com.systemdocumentut.vehicle_document_system.Services.impl;

import com.systemdocumentut.vehicle_document_system.Model.Documento;
import java.util.List;

public interface IDocumentoService {
    List<Documento> listarTodos();
    Documento actualizar(Long id, Documento documento); // Para el CRUD (PUT)
    Documento guardar(Documento documento);
    Documento buscarPorId(Long id);
    void eliminar(Long id);
}