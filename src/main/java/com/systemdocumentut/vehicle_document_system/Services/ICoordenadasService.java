package com.systemdocumentut.vehicle_document_system.Services;

import java.util.List; // IMPORTANTE: java.util.List
import org.springframework.data.domain.Pageable;
import com.systemdocumentut.vehicle_document_system.Model.Coordenadas;

public interface ICoordenadasService {
    List<Coordenadas> consultarAllCoordenadas(Pageable pageable);
}