package com.systemdocumentut.vehicle_document_system.Controller;

import com.systemdocumentut.vehicle_document_system.Model.Trayecto;
import com.systemdocumentut.vehicle_document_system.Services.impl.ITrayectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutas")
@CrossOrigin(origins = "*")
public class TrayectoController {

    @Autowired
    private ITrayectoService trayectoService;

    /**
     * REQUERIMIENTO: Consulta de rutas teniendo como parámetro de entrada el código de ruta.
     * Deberá visualizar en orden los trayectos y su respectiva información asociada.
     * PROTEGIDO: Requiere token.
     */
    @GetMapping("/{codigoRuta}")
    public ResponseEntity<?> consultarPorCodigo(
            @PathVariable String codigoRuta,
            @RequestHeader(value = "Authorization", required = true) String token) {
        
        // CORREGIDO: Se cambia 'listarPorCodigoRutaOrdenado' por 'consultarTrayectosPorCodigoRuta'
        List<Trayecto> paradas = trayectoService.consultarTrayectosPorCodigoRuta(codigoRuta);
        return ResponseEntity.ok(paradas);
    }

    /**
     * REQUERIMIENTO: Consulta teniendo como parámetro el número de identificación de un conductor.
     * Se visualizan únicamente los códigos de rutas de forma agrupada.
     * PROTEGIDO: Requiere token.
     */
    @GetMapping("/conductor/{identificacion}")
    public ResponseEntity<?> consultarRutasPorConductor(
            @PathVariable String identificacion,
            @RequestHeader(value = "Authorization", required = true) String token) {
        
        // CORREGIDO: Se cambia 'listarCodigosPorConductor' por 'consultarCodigosRutaPorConductor'
        List<String> codigos = trayectoService.consultarCodigosRutaPorConductor(identificacion);
        return ResponseEntity.ok(codigos);
    }

    /**
     * REQUERIMIENTO: Consulta teniendo como parámetro la placa de un vehículo específico.
     * Muestra el código de ruta y el conductor asociado al trayecto de forma agrupada.
     * PROTEGIDO: Requiere token.
     */
    @GetMapping("/vehiculo/{placa}")
    public ResponseEntity<?> consultarRutasPorVehiculo(
            @PathVariable String placa,
            @RequestHeader(value = "Authorization", required = true) String token) {
        
        // CORREGIDO: Se cambia 'listarRutasYConductorPorVehiculo' por 'consultarRutaYConductorPorPlaca'
        return ResponseEntity.ok(trayectoService.consultarRutaYConductorPorPlaca(placa));
    }

    /**
     * REQUERIMIENTO: Consulta de las rutas y la información de los trayectos 
     * donde el vehículo NO esté habilitado o el conductor esté Restringido para Operar.
     */
    @GetMapping("/alertas")
    public ResponseEntity<?> consultarRutasConRestriccion() {
        // CORREGIDO: Se cambia 'listarRutasConRestricciones' por 'consultarTrayectosConRestricciones'
        return ResponseEntity.ok(trayectoService.consultarTrayectosConRestricciones());
    }

    /**
     * REQUERIMIENTO: Registrar un nuevo trayecto/parada.
     * La lógica del servicio condiciona a que los documentos del vehículo estén HABILITADOS
     * y el conductor pueda operar según su estado.
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarParada(@RequestBody Trayecto trayecto) {
        try {
            // Este método ya coincidía correctamente ('guardarTrayecto')
            Trayecto nuevo = trayectoService.guardarTrayecto(trayecto);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}