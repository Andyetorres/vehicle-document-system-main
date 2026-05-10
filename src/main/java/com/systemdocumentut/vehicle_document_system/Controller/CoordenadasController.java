package com.systemdocumentut.vehicle_document_system.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.systemdocumentut.vehicle_document_system.Model.Coordenadas;
import com.systemdocumentut.vehicle_document_system.Services.ICoordenadasService;

@RestController
@RequestMapping("/LaboratorioV1")
// Mantenemos esto para reforzar la seguridad del navegador
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CoordenadasController {

    @Autowired
    private ICoordenadasService coordenadaService;
    
    @GetMapping("/coordenadas")
    public List<Coordenadas> consultarAllCoordenadas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) { // Aumenté el size para ver todos los puntos
        
        Pageable pageable = PageRequest.of(page, size);
        return coordenadaService.consultarAllCoordenadas(pageable);
    }
}