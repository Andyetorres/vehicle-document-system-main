package com.systemdocumentut.vehicle_document_system.Services;

import com.systemdocumentut.vehicle_document_system.Model.*;
import com.systemdocumentut.vehicle_document_system.Repository.*;
import com.systemdocumentut.vehicle_document_system.Services.impl.IVehiculoDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importación necesaria
import java.time.LocalDate;
import java.util.List;
import java.util.Base64;

@Service
public class VehiculoDocumentoServiceImpl implements IVehiculoDocumentoService {

    @Autowired private VehiculoDocumentoRepository repo;
    @Autowired private VehiculoRepository vehiculoRepo;
    @Autowired private DocumentoRepository documentoRepo;

    @Override
    public VehiculoDocumento asignarDocumento(Long idVehiculo, Long idDocumento, String fechaExp, String fechaVen) {
        Vehiculo v = vehiculoRepo.findById(idVehiculo)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        Documento d = documentoRepo.findById(idDocumento)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        VehiculoDocumento vd = VehiculoDocumento.builder()
                .vehiculo(v)
                .documento(d)
                .fechaExpedicion(LocalDate.parse(fechaExp))
                .fechaVencimiento(LocalDate.parse(fechaVen))
                .estado("En Verificación")
                .build();

        return repo.save(vd);
    }

    @Override
    @Transactional 
    public VehiculoDocumento guardarDocumentoConPdf(Long idVehiculo, Long idDoc, String pdfBase64, String fExp, String fVen) {
        
        Vehiculo v = vehiculoRepo.findById(idVehiculo)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        Documento d = documentoRepo.findById(idDoc)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        try {
            // Limpiar y decodificar el Base64
            String cleanBase64 = pdfBase64.contains(",") ? pdfBase64.split(",")[1] : pdfBase64;
            byte[] pdfData = Base64.getDecoder().decode(cleanBase64);

            VehiculoDocumento vd = VehiculoDocumento.builder()
                    .vehiculo(v)
                    .documento(d)
                    .documentoPdf(pdfData) 
                    .fechaExpedicion(LocalDate.parse(fExp))
                    .fechaVencimiento(LocalDate.parse(fVen))
                    .estado("En Verificación")
                    .build();

            return repo.save(vd);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar PDF: " + e.getMessage());
        }
    }

    @Override
    public List<VehiculoDocumento> listarPorEstado(String estado) {
        return repo.findByEstado(estado);
    }

    @Override
    public List<VehiculoDocumento> listarPorVehiculo(Long idVehiculo) {
        return repo.findAll().stream()
                .filter(vd -> vd.getVehiculo().getId().equals(idVehiculo))
                .toList();
    }
}