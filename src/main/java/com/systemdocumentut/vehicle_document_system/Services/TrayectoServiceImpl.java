package com.systemdocumentut.vehicle_document_system.Services;

import com.systemdocumentut.vehicle_document_system.Model.Trayecto;
import com.systemdocumentut.vehicle_document_system.Model.VehiculoConductor;
import com.systemdocumentut.vehicle_document_system.Model.VehiculoDocumento;
import com.systemdocumentut.vehicle_document_system.Repository.TrayectoRepository;
import com.systemdocumentut.vehicle_document_system.Repository.VehiculoConductorRepository;
import com.systemdocumentut.vehicle_document_system.Repository.VehiculoDocumentoRepository;
import com.systemdocumentut.vehicle_document_system.Services.impl.ITrayectoService;
import com.systemdocumentut.vehicle_document_system.Model.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service
public class TrayectoServiceImpl implements ITrayectoService {

    @Autowired
    private TrayectoRepository trayectoRepository;

    @Autowired
    private VehiculoDocumentoRepository vehiculoDocumentoRepository;

    @Autowired
    private VehiculoConductorRepository vehiculoConductorRepository;

    /**
     * Guarda un trayecto verificando las restricciones de negocio impuestas.
     * Ajustado para cumplir el contrato exacto de la interfaz.
     */
    @Override
    @Transactional
    public Trayecto guardarTrayecto(Trayecto trayecto) {
        Long idVehiculo = trayecto.getVehiculo().getId();
        Long idPersona = trayecto.getConductor().getIdPersona(); 

        // 1. Validar documentos del vehículo (Deben estar todos en estado 'Habilitado')
        List<VehiculoDocumento> documentos = vehiculoDocumentoRepository.findAll().stream()
                .filter(vd -> vd.getVehiculo().getId().equals(idVehiculo))
                .toList();

        boolean vehiculoHabilitado = !documentos.isEmpty() && 
                documentos.stream().allMatch(d -> "Habilitado".equalsIgnoreCase(d.getEstado()));

        if (!vehiculoHabilitado) {
            throw new RuntimeException("No se puede registrar el trayecto: El vehículo NO se encuentra Habilitado.");
        }

        // 2. Validar que el conductor esté autorizado para este vehículo (Estado PO)
        VehiculoConductor conductorRelacion = vehiculoConductorRepository
                .findByVehiculoIdAndPersonaId(idVehiculo, idPersona)
                .orElseThrow(() -> new RuntimeException("Relación Vehículo-Conductor no encontrada."));

        if (!"PO".equalsIgnoreCase(conductorRelacion.getEstado())) {
            throw new RuntimeException("No se puede registrar el trayecto: El conductor no tiene permisos PO (Permitido para Operar).");
        }

        // Validar límite de paradas de la ruta en curso
        List<Trayecto> trayectosExistentes = trayectoRepository.findByCodigoRutaOrderByOrdenParadaAsc(trayecto.getCodigoRuta());
        if (trayectosExistentes.size() >= 7) { // 1 inicial + 1 final + 5 intermedios máximo
            throw new IllegalArgumentException("La ruta ha excedido el límite máximo de 5 paradas intermedias.");
        }

        // Nota: Asegúrate de enviar el loginUsuario seteado en el objeto 'trayecto' desde el Controlador
        return trayectoRepository.save(trayecto);
    }

    // REQUERIMIENTO 1: Consulta de rutas ordenadas por código de ruta
    @Override
    @Transactional(readOnly = true)
    public List<Trayecto> consultarTrayectosPorCodigoRuta(String codigoRuta) {
        return trayectoRepository.findByCodigoRutaOrderByOrdenParadaAsc(codigoRuta);
    }

    // REQUERIMIENTO 2: Códigos de ruta agrupados por identificación (String) del conductor
    @Override
    @Transactional(readOnly = true)
    public List<String> consultarCodigosRutaPorConductor(String identificacion) {
        // Asumiendo que requieres buscar por la cédula/identificación string,
        // primero deberías obtener el ID interno de la persona desde su repositorio.
        // Si tu query nativa o JPQL acepta directamente el String de identificación, adáptalo en el repositorio.
        // Por ahora, si pasamos el valor directamente asumiremos que mapea al parámetro esperado.
        try {
            Long idInternoConductor = Long.parseLong(identificacion);
            return trayectoRepository.findCodigosRutaByConductorId(idInternoConductor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La identificación provista debe ser un ID numérico válido para esta consulta.");
        }
    }

    // REQUERIMIENTO 3: Código de ruta y conductor asociado filtrado por placa
    // Ajustado el retorno a Map<String, Object> como pide la interfaz
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> consultarRutaYConductorPorPlaca(String placa) {
        List<Object[]> resultados = trayectoRepository.findRutaYConductorByPlaca(placa);
        Map<String, Object> mapaAgrupado = new HashMap<>();

        for (Object[] fila : resultados) {
            String codigoRuta = (String) fila[0];
            Persona conductor = (Persona) fila[1];
            String nombreCompleto = conductor.getNombres() + " " + conductor.getApellidos();

            // Mantenemos la estructura de listas internas para los nombres mapeados
            if (!mapaAgrupado.containsKey(codigoRuta)) {
                mapaAgrupado.put(codigoRuta, new ArrayList<String>());
            }
            ((List<String>) mapaAgrupado.get(codigoRuta)).add(nombreCompleto);
        }

        return mapaAgrupado;
    }

    // REQUERIMIENTO 4: Rutas y trayectos con restricciones activas
    @Override
    @Transactional(readOnly = true)
    public List<Trayecto> consultarTrayectosConRestricciones() {
        return trayectoRepository.findTrayectosConRestricciones();
    }

    // --- MÉTODOS DEL CRON JOB (REQUERIMIENTO TÉCNICO) ---

    @Override
    @Transactional(readOnly = true)
    public List<Trayecto> listarTrayectosSinCoordenadas() {
        return trayectoRepository.findTrayectosSinCoordenadas();
    }

    @Override
    @Transactional
    public void actualizarCoordenadas(Long trayectoId, Double latitud, Double longitud) {
        Trayecto trayecto = trayectoRepository.findById(trayectoId)
                .orElseThrow(() -> new RuntimeException("Trayecto no encontrado con ID: " + trayectoId));
        
        trayecto.setLatitud(latitud);
        trayecto.setLongitud(longitud);
        trayectoRepository.save(trayecto);
    }
}