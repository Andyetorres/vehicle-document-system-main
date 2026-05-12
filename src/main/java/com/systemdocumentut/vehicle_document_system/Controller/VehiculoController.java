package com.systemdocumentut.vehicle_document_system.Controller;

import com.systemdocumentut.vehicle_document_system.DTOs.VehiculoDTO;
import com.systemdocumentut.vehicle_document_system.Services.impl.IVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la gestión de Vehículos y su relación con Documentos.
 * Cumple con los requerimientos de CRUD y búsquedas parametrizadas.
 */
@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "*") 
public class VehiculoController {

    @Autowired
    private IVehiculoService vehiculoService;

    // --- REQUERIMIENTO: CRUD (CREATE) ---
    // Importante: No se puede crear un vehículo sin un documento asociado.
    // El estado inicial del documento será "En Verificación" (gestionado en el Service).
    @PostMapping("/con-documento/{idDoc}")
    public ResponseEntity<?> crearConDocumento(@RequestBody VehiculoDTO dto, @PathVariable Long idDoc) {
        try {
            VehiculoDTO nuevoVehiculo = vehiculoService.crearVehiculoConDocumento(dto, idDoc);
            return new ResponseEntity<>(nuevoVehiculo, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Aquí capturamos errores de validación de placa o lógica de negocio
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el vehículo: " + e.getMessage());
        }
    }

    // --- REQUERIMIENTO: CRUD (READ ALL) ---
    @GetMapping
    public ResponseEntity<List<VehiculoDTO>> listar() {
        return ResponseEntity.ok(vehiculoService.listarTodos());
    }

    // --- REQUERIMIENTO: BUSCAR POR PLACA ---
    @GetMapping("/placa/{placa}")
    public ResponseEntity<?> buscarPorPlaca(@PathVariable String placa) {
        try {
            VehiculoDTO vehiculo = vehiculoService.buscarPorPlaca(placa);
            return ResponseEntity.ok(vehiculo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehículo con placa " + placa + " no encontrado.");
        }
    }

    // --- REQUERIMIENTO: BUSCAR POR TIPO DE VEHÍCULO (Automóvil/Motocicleta) ---
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<VehiculoDTO>> buscarPorTipo(@PathVariable String tipo) {
        List<VehiculoDTO> vehiculos = vehiculoService.buscarPorTipoVehiculo(tipo);
        return ResponseEntity.ok(vehiculos);
    }

    // --- REQUERIMIENTO: BUSCAR POR ESTADO DEL DOCUMENTO ---
    // Filtra documentos: Habilitado, Vencido o En Verificación
    @GetMapping("/estado-documento/{estado}")
    public ResponseEntity<?> buscarPorEstadoDocumento(@PathVariable String estado) {
        try {
            List<VehiculoDTO> vehiculos = vehiculoService.buscarPorEstadoDocumento(estado);
            return ResponseEntity.ok(vehiculos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Estado no válido. Use: Habilitado, Vencido o En Verificación.");
        }
    }

    // --- REQUERIMIENTO: BUSCAR POR TIPO DE DOCUMENTO EN COMÚN ---
    @GetMapping("/por-documento/{idDoc}")
    public ResponseEntity<List<VehiculoDTO>> buscarPorDocumento(@PathVariable Long idDoc) {
        List<VehiculoDTO> vehiculos = vehiculoService.buscarVehiculosPorTipoDocumento(idDoc);
        return ResponseEntity.ok(vehiculos);
    }

    // --- REQUERIMIENTO: CRUD (UPDATE) ---
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody VehiculoDTO dto) {
        try {
            VehiculoDTO actualizado = vehiculoService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // --- REQUERIMIENTO: CRUD (DELETE) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            vehiculoService.eliminar(id);
            return ResponseEntity.ok("Vehículo con ID " + id + " eliminado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo eliminar: Vehículo no encontrado.");
        }
    }

    // --- REQUERIMIENTO: AGREGAR DOCUMENTOS ADICIONALES A UN VEHÍCULO ---
    @PostMapping("/{idVehiculo}/nuevo-documento/{idDoc}")
    public ResponseEntity<?> asociarNuevoDocumento(
            @PathVariable Long idVehiculo, 
            @PathVariable Long idDoc,
            @RequestParam String fechaExp,
            @RequestParam String fechaVen)
             {
        try {
            vehiculoService.agregarDocumentoAVehiculo(idVehiculo, idDoc, fechaExp, fechaVen);
            return ResponseEntity.ok("Documento asociado exitosamente con estado inicial: En Verificación.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}