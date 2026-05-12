package com.systemdocumentut.vehicle_document_system.Services;

import com.systemdocumentut.vehicle_document_system.Model.Documento;
import com.systemdocumentut.vehicle_document_system.Repository.DocumentoRepository;
import com.systemdocumentut.vehicle_document_system.Services.impl.IDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DocumentoServiceImpl implements IDocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Documento> listarTodos() {
        return documentoRepository.findAll();
    }

    @Override
    @Transactional
    public Documento guardar(Documento documento) {
        // Aquí podrías agregar lógica para validar que los códigos 'A', 'M', 'AM' 
        // lleguen correctamente antes de intentar el insert en DB
        validarCamposParametricos(documento);
        return documentoRepository.save(documento);
    }

    @Override
    @Transactional(readOnly = true)
    public Documento buscarPorId(Long id) {
        return documentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento paramétrico no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public Documento actualizar(Long id, Documento nuevoDoc) {
        return documentoRepository.findById(id).map(docExistente -> {
            validarCamposParametricos(nuevoDoc);
            
            docExistente.setCodigo(nuevoDoc.getCodigo());
            docExistente.setNombre(nuevoDoc.getNombre());
            docExistente.setAplicaA(nuevoDoc.getAplicaA()); // A, M o AM
            docExistente.setObligatorio(nuevoDoc.getObligatorio()); // RA, RM o RR
            docExistente.setDescripcion(nuevoDoc.getDescripcion());
            
            return documentoRepository.save(docExistente);
        }).orElseThrow(() -> new RuntimeException("No se puede actualizar: Documento no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!documentoRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Documento no encontrado");
        }
        documentoRepository.deleteById(id);
    }

    /**
     * Validación de lógica de negocio para los campos restringidos 
     * antes de que lleguen a la base de datos.
     */
    private void validarCamposParametricos(Documento doc) {
        // Validar Tipos de Vehículos (A, M, AM)
        if (!doc.getAplicaA().matches("^(A|M|AM)$")) {
            throw new IllegalArgumentException("Valor inválido para aplicaA. Use: A, M o AM");
        }
        // Validar Obligatoriedad (RA, RM, RR)
        if (!doc.getObligatorio().matches("^(RA|RM|RR)$")) {
            throw new IllegalArgumentException("Valor inválido para obligatorio. Use: RA, RM o RR");
        }
    }
}