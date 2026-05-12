package com.systemdocumentut.vehicle_document_system.Services.impl;

import com.systemdocumentut.vehicle_document_system.Model.Documento;
import java.util.List;

public interface IDocumentoService {
    // CRUD Básico para la entidad paramétrica
    List<Documento> listarTodos();
    Documento guardar(Documento documento);
    Documento buscarPorId(Long id);
    Documento actualizar(Long id, Documento documento);
    void eliminar(Long id);
}