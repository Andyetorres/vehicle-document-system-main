package com.systemdocumentut.vehicle_document_system.Services;

import com.systemdocumentut.vehicle_document_system.Model.Documento;
import com.systemdocumentut.vehicle_document_system.Repository.DocumentoRepository;
import com.systemdocumentut.vehicle_document_system.Services.impl.IDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DocumentoServiceImpl implements IDocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Override
    public List<Documento> listarTodos() {
        return documentoRepository.findAll();
    }

    @Override
    public Documento guardar(Documento documento) {
        return documentoRepository.save(documento);
    }

    @Override
    public Documento buscarPorId(Long id) {
        return documentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento paramétrico no encontrado"));
    }

    @Override
    public void eliminar(Long id) {
        documentoRepository.deleteById(id);
    }

    @Override
    public Documento actualizar(Long id, Documento nuevoDoc) {
        // 1. Buscamos si existe, si no, lanzamos excepción
        return documentoRepository.findById(id).map(docExistente -> {
            // 2. Actualizamos los campos
            docExistente.setCodigo(nuevoDoc.getCodigo());
            docExistente.setNombre(nuevoDoc.getNombre());
            docExistente.setAplicaA(nuevoDoc.getAplicaA());
            docExistente.setObligatorio(nuevoDoc.getObligatorio());
            docExistente.setDescripcion(nuevoDoc.getDescripcion());
            
            // 3. Guardamos los cambios
            return documentoRepository.save(docExistente);
        }).orElseThrow(() -> new RuntimeException("No se encontró el documento con ID: " + id));
    }
}