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
@CrossOrigin(origins = "http://127.0.0.1:5500", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS})
public class CoordenadasController {

    @Autowired
    private ICoordenadasService coordenadaService;
    
    @CrossOrigin(origins = "*")
    @GetMapping("/coordenadas")
    public List<Coordenadas> consultarAllCoordenadas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return coordenadaService.consultarAllCoordenadas(pageable);
    }
}