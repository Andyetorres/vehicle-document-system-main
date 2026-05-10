package com.systemdocumentut.vehicle_document_system.Services.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.systemdocumentut.vehicle_document_system.Model.Coordenadas;
import com.systemdocumentut.vehicle_document_system.Repository.CoordenadasRepository;
import com.systemdocumentut.vehicle_document_system.Services.ICoordenadasService;

@Service("CoordenadasService")
public class CoordenadasServiceImpl implements ICoordenadasService {

    @Autowired
    @Qualifier("ICoordenadasRepository")
    private CoordenadasRepository coordenadasRepository; // Cambié el nombre para que coincida abajo

    @Override
    public List<Coordenadas> consultarAllCoordenadas(Pageable pageable) {
        // .getContent() se usa porque findAll(pageable) devuelve un objeto Page
        return coordenadasRepository.findAll(pageable).getContent();
    }
}