package com.systemdocumentut.vehicle_document_system.Services;

import com.systemdocumentut.vehicle_document_system.Model.*;
import com.systemdocumentut.vehicle_document_system.Repository.*;
import com.systemdocumentut.vehicle_document_system.Services.impl.IVehiculoService;
import com.systemdocumentut.vehicle_document_system.DTOs.VehiculoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiculoServiceImpl implements IVehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;
    @Autowired
    private DocumentoRepository documentoRepository;
    @Autowired
    private VehiculoDocumentoRepository vehiculoDocumentoRepository;

    @Override
    @Transactional
    public VehiculoDTO crearVehiculoConDocumento(VehiculoDTO dto, Long idDocumentoBase) {
        // 1. Validar Documento Obligatorio
        if (idDocumentoBase == null)
            throw new RuntimeException("No se puede crear un vehículo sin un documento asociado.");

        // 2. Validar Placa y Color (Reglas del negocio)
        validarNegocio(dto);

        // 3. Mapear y Guardar Vehículo
        Vehiculo vehiculo = mapToEntity(dto);
        Vehiculo nuevoVehiculo = vehiculoRepository.save(vehiculo);

        // 4. Buscar el Documento Maestro
        Documento doc = documentoRepository.findById(idDocumentoBase)
                .orElseThrow(() -> new RuntimeException("El documento especificado no existe."));

        // 5. Crear relación con estado inicial "En Verificación"
        VehiculoDocumento relacion = VehiculoDocumento.builder()
                .vehiculo(nuevoVehiculo)
                .documento(doc)
                .estado("En Verificación")
                .fechaExpedicion(LocalDate.now())
                .fechaVencimiento(LocalDate.now().plusYears(1))
                .build();

        vehiculoDocumentoRepository.save(relacion);

        return mapToDTO(nuevoVehiculo);
    }

    private void validarNegocio(VehiculoDTO dto) {
        // Placa: 6 caracteres exactos
        String placa = dto.getPlaca();
        if (placa == null || placa.length() != 6)
            throw new RuntimeException("La placa debe tener 6 caracteres.");

        // Formato por tipo
        if ("Automóvil".equalsIgnoreCase(dto.getTipoVehiculo())) {
            if (!placa.matches("^[A-Z]{3}[0-9]{3}$"))
                throw new RuntimeException("Placa de auto debe ser 3 letras y 3 números.");
        } else if ("Motocicleta".equalsIgnoreCase(dto.getTipoVehiculo())) {
            if (!placa.matches("^[A-Z]{3}[0-9]{2}[A-Z]$"))
                throw new RuntimeException("Placa de moto debe ser 3 letras, 2 números y 1 letra.");
        }

        // Color Hexadecimal
        if (!dto.getColor().matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
            throw new RuntimeException("El color debe ser un código hexadecimal válido (ej: #FFFFFF).");
        }
    }

    @Override
    public VehiculoDTO buscarPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("No se encontró vehículo con placa: " + placa));
    }

    @Override
    public List<VehiculoDTO> buscarPorTipo(String tipo) {
        return vehiculoRepository.findByTipoVehiculo(tipo).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehiculoDTO> buscarPorEstadoDocumento(String estado) {
        // Busca en la tabla intermedia y extrae los vehículos
        return vehiculoDocumentoRepository.findByEstado(estado).stream()
                .map(rel -> mapToDTO(rel.getVehiculo()))
                .collect(Collectors.toList());
    }

    // --- Métodos de apoyo y CRUD básico ---
    @Override
    public List<VehiculoDTO> listarTodos() {
        return vehiculoRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    

    @Override
    public VehiculoDTO guardar(VehiculoDTO dto) {
        return mapToDTO(vehiculoRepository.save(mapToEntity(dto)));
    }

    private VehiculoDTO mapToDTO(Vehiculo v) {
        return VehiculoDTO.builder()
                .id(v.getId()).placa(v.getPlaca()).marca(v.getMarca()).linea(v.getLinea())
                .modelo(v.getModelo()).color(v.getColor()).tipoVehiculo(v.getTipoVehiculo())
                .tipoServicio(v.getTipoServicio()).tipoCombustible(v.getTipoCombustible())
                .capacidadPasajeros(v.getCapacidadPasajeros())
                .build();
    }

    private Vehiculo mapToEntity(VehiculoDTO d) {
        return Vehiculo.builder()
                .placa(d.getPlaca().toUpperCase()).marca(d.getMarca()).linea(d.getLinea())
                .modelo(d.getModelo()).color(d.getColor()).tipoVehiculo(d.getTipoVehiculo())
                .tipoServicio(d.getTipoServicio()).tipoCombustible(d.getTipoCombustible())
                .capacidadPasajeros(d.getCapacidadPasajeros())
                .build();
    }

    @Override
    public VehiculoDTO actualizar(Long id, VehiculoDTO dto) {
        Vehiculo v = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        validarNegocio(dto); // Reutilizamos tu lógica de validación de placa/color

        v.setTipoVehiculo(dto.getTipoVehiculo());
        v.setTipoServicio(dto.getTipoServicio());
        v.setTipoCombustible(dto.getTipoCombustible());
        v.setCapacidadPasajeros(dto.getCapacidadPasajeros());
        v.setColor(dto.getColor());
        v.setModelo(dto.getModelo());
        v.setMarca(dto.getMarca());
        v.setLinea(dto.getLinea());

        return mapToDTO(vehiculoRepository.save(v));
    }

    @Override
    public List<VehiculoDTO> buscarPorTipoDocumento(Long idDocumento) {
        return vehiculoRepository.findByDocumentoId(idDocumento).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
@Transactional
public void eliminar(Long id) {
    // 1. Verificar si existe antes de borrar
    if (!vehiculoRepository.existsById(id)) {
        throw new RuntimeException("No se puede eliminar: Vehículo no encontrado con ID: " + id);
    }
    List<VehiculoDocumento> relaciones = vehiculoDocumentoRepository.findAll()
            .stream()
            .filter(vd -> vd.getVehiculo().getId().equals(id))
            .toList();
    
    vehiculoDocumentoRepository.deleteAll(relaciones);
    
    // 2. Borrar primero las relaciones en la tabla intermedia (si no tienes Cascade)
    // Nota: Si no tienes este método en el repo, puedes borrar por ID de vehículo
    vehiculoRepository.deleteById(id);
}
    
}