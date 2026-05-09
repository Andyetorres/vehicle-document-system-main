
package com.systemdocumentut.vehicle_document_system.Controller;

import com.systemdocumentut.vehicle_document_system.Services.impl.IVehiculoDocumentoService;
import com.systemdocumentut.vehicle_document_system.Services.impl.IVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gestion-documentos")
public class VehiculoDocumentoController {

    @Autowired
    private IVehiculoDocumentoService gestionService;
    
    @Autowired
    private IVehiculoService vehiculoService;

    // Requerimiento: Buscar vehículos por estado (Habilitado, Vencido, En Verificación)
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(vehiculoService.buscarPorEstadoDocumento(estado));
    }

    // Endpoint para asignar un nuevo documento a un vehículo ya creado
    @PostMapping("/asignar")
    public ResponseEntity<?> asignar(@RequestParam Long idVehiculo, 
                                     @RequestParam Long idDoc,
                                     @RequestParam String fechaExp,
                                     @RequestParam String fechaVen) {
        try {
            return ResponseEntity.ok(gestionService.asignarDocumento(idVehiculo, idDoc, fechaExp, fechaVen));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}