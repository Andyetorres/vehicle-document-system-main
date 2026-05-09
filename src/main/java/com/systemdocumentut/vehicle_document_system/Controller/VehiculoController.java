package com.systemdocumentut.vehicle_document_system.Controller;

import com.systemdocumentut.vehicle_document_system.DTOs.VehiculoDTO;
import com.systemdocumentut.vehicle_document_system.Services.impl.IVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "*") // Para evitar bloqueos de navegador
public class VehiculoController {

    @Autowired
    private IVehiculoService vehiculoService;

    // Requerimiento: Crear vehículo con documento obligatorio
    @PostMapping("/con-documento/{idDoc}")
    public ResponseEntity<?> crearConDocumento(@RequestBody VehiculoDTO dto, @PathVariable Long idDoc) {
        try {
            return ResponseEntity.ok(vehiculoService.crearVehiculoConDocumento(dto, idDoc));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<VehiculoDTO> listar() {
        return vehiculoService.listarTodos();
    }

    // Requerimiento: Buscar por placa
    @GetMapping("/placa/{placa}")
    public ResponseEntity<?> buscarPorPlaca(@PathVariable String placa) {
        try {
            return ResponseEntity.ok(vehiculoService.buscarPorPlaca(placa));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Requerimiento: Buscar por tipo (Automóvil/Motocicleta)
    @GetMapping("/tipo/{tipo}")
    public List<VehiculoDTO> buscarPorTipo(@PathVariable String tipo) {
        return vehiculoService.buscarPorTipo(tipo);
    }

    @GetMapping("/vencidos")
    public ResponseEntity<?> listarVencidos() {
        // Aquí llamas a tu servicio que busca vehículos con estado "Vencido"
        return ResponseEntity.ok(vehiculoService.buscarPorEstadoDocumento("Vencido"));
    }

    //Actualizar

    @PutMapping("/{id}")
    public ResponseEntity<VehiculoDTO> actualizar(@PathVariable Long id, @RequestBody VehiculoDTO dto) {
        return ResponseEntity.ok(vehiculoService.actualizar(id, dto));
    }

    // Requerimiento: Buscar vehículos que tengan en común un tipo de documento
    @GetMapping("/por-documento/{idDoc}")
    public List<VehiculoDTO> buscarPorDocumento(@PathVariable Long idDoc) {
        return vehiculoService.buscarPorTipoDocumento(idDoc);
    }

    @DeleteMapping("/{id}")
public ResponseEntity<?> eliminar(@PathVariable Long id) {
    try {
        vehiculoService.eliminar(id);
        return ResponseEntity.ok("Vehículo eliminado correctamente");
    } catch (Exception e) {
        return ResponseEntity.notFound().build();
    }
}

}