package com.systemdocumentut.vehicle_document_system.Controller;

import com.systemdocumentut.vehicle_document_system.DTOs.VehiculoDTO;
import com.systemdocumentut.vehicle_document_system.Model.Vehiculo;
import com.systemdocumentut.vehicle_document_system.Services.impl.IVehiculoDocumentoService;
import com.systemdocumentut.vehicle_document_system.Services.impl.IVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos-documentos")
public class VehiculoDocumentoController {

    @Autowired
    private IVehiculoDocumentoService gestionService;
    
    @Autowired
    private IVehiculoService vehiculoService;

    // --- SERVICIOS DE BÚSQUEDA ESPECÍFICOS ---

    /**
     * Buscar vehículo por número de placa
     * GET /api/vehiculos-documentos/placa/{placa}
     */
    @GetMapping("/placa/{placa}")
    public ResponseEntity<?> buscarPorPlaca(@PathVariable String placa) {
        VehiculoDTO v = vehiculoService.buscarPorPlaca(placa);
        if (v == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehículo no encontrado");
        return ResponseEntity.ok(v);
    }

    /**
     * Buscar vehículos por tipo (Automóvil - Motocicleta)
     * GET /api/vehiculos-documentos/tipo/{tipo}
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<VehiculoDTO>> buscarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(vehiculoService.buscarPorTipoVehiculo(tipo));
    }

    /**
     * Buscar vehículos que tengan en común un tipo de documento
     * GET /api/vehiculos-documentos/documento/{idDocumento}
     */
    @GetMapping("/documento/{idDocumento}")
    public ResponseEntity<List<VehiculoDTO>> buscarPorDocumentoComun(@PathVariable Long idDocumento) {
        return ResponseEntity.ok(vehiculoService.buscarVehiculosPorTipoDocumento(idDocumento));
    }

    /**
     * Buscar vehículos según el estado del documento (Habilitado, Vencido, En Verificación)
     * GET /api/vehiculos-documentos/estado/{estado}
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<VehiculoDTO>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(vehiculoService.buscarPorEstadoDocumento(estado));
    }

    // --- SERVICIOS DE GESTIÓN DE RELACIÓN ---

    /**
     * Crear un servicio que permita agregar documentos asociados a un vehículo.
     * POST /api/vehiculos-documentos/asignar
     */
    @PostMapping("/asignar")
    public ResponseEntity<?> asignarDocumentoAVehiculo(
            @RequestParam Long idVehiculo, 
            @RequestParam Long idDoc,
            @RequestParam String fechaExp,
            @RequestParam String fechaVen) {
        try {
            // El service debe validar que el estado inicial sea "En Verificación"
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(gestionService.asignarDocumento(idVehiculo, idDoc, fechaExp, fechaVen));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al asignar documento: " + e.getMessage());
        }
    }

    /**
     * Obtener todos los vehículos con sus documentos
     * GET /api/vehiculos-documentos
     */
    @GetMapping
    public ResponseEntity<List<VehiculoDTO>> listarTodos() {
        return ResponseEntity.ok(vehiculoService.listarTodos());
    }
}