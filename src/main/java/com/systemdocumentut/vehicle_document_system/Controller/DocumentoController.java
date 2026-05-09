package com.systemdocumentut.vehicle_document_system.Controller;

import com.systemdocumentut.vehicle_document_system.Model.Documento;
import com.systemdocumentut.vehicle_document_system.Services.impl.IDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @Autowired
    private IDocumentoService documentoService;

    @GetMapping
    public List<Documento> listar() {
        return documentoService.listarTodos();
    }

    @PostMapping
    public Documento guardar(@RequestBody Documento documento) {
        documento.setId(null);
        return documentoService.guardar(documento);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        documentoService.eliminar(id);
    }

    @PutMapping("/{id}")
    public Documento actualizar(@PathVariable Long id, @RequestBody Documento documento) {
        return documentoService.actualizar(id, documento);
    }
}