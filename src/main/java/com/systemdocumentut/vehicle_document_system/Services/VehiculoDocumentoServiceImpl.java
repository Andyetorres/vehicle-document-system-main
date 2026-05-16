package com.systemdocumentut.vehicle_document_system.Services;

import com.systemdocumentut.vehicle_document_system.Model.Documento;
import com.systemdocumentut.vehicle_document_system.Model.Vehiculo;
import com.systemdocumentut.vehicle_document_system.Model.VehiculoDocumento;
import com.systemdocumentut.vehicle_document_system.Repository.DocumentoRepository;
import com.systemdocumentut.vehicle_document_system.Repository.VehiculoDocumentoRepository;
import com.systemdocumentut.vehicle_document_system.Repository.VehiculoRepository;
import com.systemdocumentut.vehicle_document_system.Services.impl.IVehiculoDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class VehiculoDocumentoServiceImpl implements IVehiculoDocumentoService {

    @Autowired 
    private VehiculoDocumentoRepository repo;
    
    @Autowired 
    private VehiculoRepository vehiculoRepo;
    
    @Autowired 
    private DocumentoRepository documentoRepo;

    @Override
    @Transactional
    public VehiculoDocumento asignarDocumento(Long idVehiculo, Long idDocumento, String fechaExp, String fechaVen) {
        Vehiculo v = vehiculoRepo.findById(idVehiculo)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + idVehiculo));
        
        Documento d = documentoRepo.findById(idDocumento)
                .orElseThrow(() -> new RuntimeException("Documento parametrizado no encontrado con ID: " + idDocumento));

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
            if (pdfBase64 == null || !pdfBase64.contains("JVBERi0")) {
                 throw new RuntimeException("El contenido no parece ser un PDF válido.");
            }

            VehiculoDocumento vd = VehiculoDocumento.builder()
                    .vehiculo(v)
                    .documento(d)
                    .archivoPdf(pdfBase64) 
                    .fechaExpedicion(LocalDate.parse(fExp))
                    .fechaVencimiento(LocalDate.parse(fVen))
                    .estado("En Verificación")
                    .build();

            return repo.save(vd);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el documento: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoDocumento> listarPorEstado(String estado) {
        return repo.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoDocumento> listarPorVehiculo(Long idVehiculo) {
        return repo.findAll().stream()
                .filter(vd -> vd.getVehiculo().getId().equals(idVehiculo))
                .toList();
    }

    // --- NUEVAS IMPLEMENTACIONES (ENTREGA 3) ---

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoDocumento> listarDocumentosVehiculoExpirados() {
        // Ejecuta la consulta basada en la fecha del sistema actual
        return repo.findDocumentosExpirados(LocalDate.now());
    }

    @Override
    @Transactional
    public void marcarDocumentoComoVencido(Long vehiculoDocumentoId) {
        VehiculoDocumento vd = repo.findById(vehiculoDocumentoId)
                .orElseThrow(() -> new RuntimeException("Registro de documento de vehículo no encontrado con ID: " + vehiculoDocumentoId));
        
        vd.setEstado("VENCIDO"); // Requerimiento explícito: estado cambiado a VENCIDO
        repo.save(vd);
    }
}