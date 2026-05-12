package com.systemdocumentut.vehicle_document_system.Services;

import com.systemdocumentut.vehicle_document_system.Model.*;
import com.systemdocumentut.vehicle_document_system.Repository.*;
import com.systemdocumentut.vehicle_document_system.Services.impl.IVehiculoService;
import com.systemdocumentut.vehicle_document_system.DTOs.*; // Asegúrate de tener DocumentoCargaDTO, DocumentoMetaDTO, etc.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiculoServiceImpl implements IVehiculoService {

    @Autowired private VehiculoRepository vehiculoRepository;
    @Autowired private DocumentoRepository documentoRepository;
    @Autowired private VehiculoDocumentoRepository vehiculoDocumentoRepository;
    @Autowired private PersonaRepository personaRepository;
    @Autowired private VehiculoConductorRepository vehiculoConductorRepository;

    // ==========================================
    // MÉTODOS REQUERIDOS POR LA INTERFAZ (CRUD)
    // ==========================================

    @Override
    public List<VehiculoDTO> listarTodos() {
        return vehiculoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VehiculoDTO buscarPorId(Long id) {
        return vehiculoRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + id));
    }

    @Override
    public VehiculoDTO buscarPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa.toUpperCase())
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Placa no encontrada: " + placa));
    }

    @Override
    @Transactional
    public VehiculoDTO actualizar(Long id, VehiculoDTO dto) {
        Vehiculo v = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el vehículo para actualizar"));
        validarReglasNegocio(dto);
        
        v.setMarca(dto.getMarca());
        v.setLinea(dto.getLinea());
        v.setModelo(dto.getModelo());
        v.setColor(dto.getColor());
        v.setTipoVehiculo(dto.getTipoVehiculo());
        
        return mapToDTO(vehiculoRepository.save(v));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!vehiculoRepository.existsById(id)) throw new RuntimeException("ID no existe");
        vehiculoRepository.deleteById(id);
    }

    // ==========================================
    // BÚSQUEDAS ESPECÍFICAS (Faltantes en tu código)
    // ==========================================

    @Override
    public List<VehiculoDTO> buscarPorTipoVehiculo(String tipo) {
        return vehiculoRepository.findByTipoVehiculo(tipo).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehiculoDTO> buscarPorEstadoDocumento(String estado) {
        return vehiculoDocumentoRepository.findByEstado(estado).stream()
                .map(vd -> mapToDTO(vd.getVehiculo()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<VehiculoDTO> buscarVehiculosPorTipoDocumento(Long idDocumento) {
        return vehiculoDocumentoRepository.findAll().stream()
                .filter(vd -> vd.getDocumento().getId().equals(idDocumento))
                .map(vd -> mapToDTO(vd.getVehiculo()))
                .collect(Collectors.toList());
    }

    // ==========================================
    // LÓGICA DE DOCUMENTOS Y CONDUCTORES
    // ==========================================

    @Override
    @Transactional
    public void agregarDocumentoAVehiculo(Long vehiculoId, Long documentoId, String fechaExp, String fechaVen) {
        Vehiculo v = vehiculoRepository.findById(vehiculoId).orElseThrow(() -> new RuntimeException("Vehículo no hallado"));
        Documento d = documentoRepository.findById(documentoId).orElseThrow(() -> new RuntimeException("Tipo documento no hallado"));

        VehiculoDocumento vd = new VehiculoDocumento();
        vd.setVehiculo(v);
        vd.setDocumento(d);
        vd.setEstado("EA");
        vd.setFechaExpedicion(LocalDate.parse(fechaExp));
        vd.setFechaVencimiento(LocalDate.parse(fechaVen));
        vehiculoDocumentoRepository.save(vd);
    }

    // REQUERIMIENTO: Carga Masiva PDF Base64
    @Transactional
    public void cargarDocumentosBase64(Long vehiculoId, List<DocumentoCargaDTO> documentos) {
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId).orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        for (DocumentoCargaDTO dto : documentos) {
            if (dto.getBase64() == null || !dto.getBase64().contains("JVBERi0")) throw new RuntimeException("PDF Inválido");
            Documento docMaestro = documentoRepository.findById(dto.getIdDocumento()).orElseThrow(() -> new RuntimeException("Doc Maestro no existe"));
            
            VehiculoDocumento vd = new VehiculoDocumento();
            vd.setVehiculo(vehiculo);
            vd.setDocumento(docMaestro);
            vd.setEstado("EA");
            vd.setFechaExpedicion(dto.getFechaExpedicion());
            vd.setFechaVencimiento(dto.getFechaVencimiento());
            vd.setArchivoPdf(dto.getBase64()); // Campo BLOB
            vehiculoDocumentoRepository.save(vd);
        }
    }

    @Override
    @Transactional
    public void asociarConductor(Long vehiculoId, Long personaId) {
        Vehiculo v = vehiculoRepository.findById(vehiculoId).orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        Persona p = personaRepository.findById(personaId).orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        if (!"C".equalsIgnoreCase(p.getTipoPersona())) throw new RuntimeException("Solo se permiten conductores");

        VehiculoConductor asociación = new VehiculoConductor();
        asociación.setVehiculo(v);
        asociación.setPersona(p);
        asociación.setFechaAsociacion(LocalDate.now());
        asociación.setEstado("EA");
        vehiculoConductorRepository.save(asociación);
    }

    @Override
    @Transactional
    public VehiculoDTO crearVehiculoConDocumento(VehiculoDTO dto, Long idDocumentoBase) {
        validarReglasNegocio(dto);
        Vehiculo vehiculo = mapToEntity(dto);
        Vehiculo nuevoVehiculo = vehiculoRepository.save(vehiculo);
        Documento docMaestro = documentoRepository.findById(idDocumentoBase).orElseThrow(() -> new RuntimeException("Doc no existe"));

        VehiculoDocumento relacion = new VehiculoDocumento();
        relacion.setVehiculo(nuevoVehiculo);
        relacion.setDocumento(docMaestro);
        relacion.setEstado("EA");
        relacion.setFechaExpedicion(LocalDate.now());
        relacion.setFechaVencimiento(LocalDate.now().plusYears(1));
        vehiculoDocumentoRepository.save(relacion);

        return mapToDTO(nuevoVehiculo);
    }

    // ==========================================
    // UTILS Y MAPPERS
    // ==========================================

    private void validarReglasNegocio(VehiculoDTO dto) {
        if (dto.getPlaca() == null || dto.getPlaca().length() != 6) throw new RuntimeException("Placa de 6 caracteres");
        if (!dto.getColor().matches("^#([A-Fa-f0-9]{6})$")) throw new RuntimeException("Color Hexadecimal inválido");
    }

    private VehiculoDTO mapToDTO(Vehiculo v) {
        return VehiculoDTO.builder()
                .id(v.getId()).placa(v.getPlaca()).marca(v.getMarca()).linea(v.getLinea())
                .modelo(v.getModelo()).color(v.getColor()).tipoVehiculo(v.getTipoVehiculo())
                .build();
    }

    private Vehiculo mapToEntity(VehiculoDTO d) {
        return Vehiculo.builder()
                .placa(d.getPlaca().toUpperCase()).marca(d.getMarca()).linea(d.getLinea())
                .modelo(d.getModelo()).color(d.getColor()).tipoVehiculo(d.getTipoVehiculo())
                .build();
    }
}