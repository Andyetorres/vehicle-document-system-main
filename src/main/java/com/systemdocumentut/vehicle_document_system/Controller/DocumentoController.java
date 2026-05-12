package com.systemdocumentut.vehicle_document_system.Controller;

import com.systemdocumentut.vehicle_document_system.Model.Documento;
import com.systemdocumentut.vehicle_document_system.Services.impl.IDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @Autowired
    private IDocumentoService documentoService;

    /**
     * GET: Listar todos los documentos parametrizados.
     */
    @GetMapping
    public ResponseEntity<List<Documento>> listar() {
        List<Documento> lista = documentoService.listarTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    /**
     * GET por ID: Útil para el CRUD completo.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Documento> obtenerPorId(@PathVariable Long id) {
        Documento documento = documentoService.buscarPorId(id);
        if (documento != null) {
            return new ResponseEntity<>(documento, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * POST: Crear un nuevo documento paramétrico.
     * Se asegura de que el ID sea nulo para forzar creación.
     */
    @PostMapping
    public ResponseEntity<Documento> guardar(@RequestBody Documento documento) {
        try {
            Documento nuevoDocumento = documentoService.guardar(documento);
            return new ResponseEntity<>(nuevoDocumento, HttpStatus.CREATED);
        } catch (Exception e) {
            // Solución al error de inferencia:
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    /**
     * PUT: Actualizar un documento existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Documento> actualizar(@PathVariable Long id, @RequestBody Documento documento) {
        Documento actualizado = documentoService.actualizar(id, documento);
        if (actualizado != null) {
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * DELETE: Eliminar un documento por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            documentoService.eliminar(id);
            return ResponseEntity.noContent().build(); // Más limpio y evita errores de inferencia
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}