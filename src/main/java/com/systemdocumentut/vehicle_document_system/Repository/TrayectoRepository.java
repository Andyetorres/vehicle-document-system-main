package com.systemdocumentut.vehicle_document_system.Repository;

import com.systemdocumentut.vehicle_document_system.Model.Trayecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ITrayectoRepository")
public interface TrayectoRepository extends JpaRepository<Trayecto, Long> {

    /**
     * Requerimiento Servicio 1: Consulta de rutas por código de ruta.
     * Visualiza en orden los trayectos usando el campo ordenParada de forma ascendente.
     */
    @Query("SELECT t FROM Trayecto t WHERE t.codigoRuta = :codigoRuta ORDER BY t.ordenParada ASC")
    List<Trayecto> findByCodigoRutaOrderByOrdenParadaAsc(@Param("codigoRuta") String codigoRuta);

    /**
     * Requerimiento Servicio 2: Consulta teniendo como parámetro el número de identificación 
     * de un conductor específico donde se visualizan únicamente los códigos de rutas agrupados.
     * CORREGIDO: Se cambió 't.persona.id' por 't.conductor.idPersona'
     */
    @Query("SELECT DISTINCT t.codigoRuta FROM Trayecto t WHERE t.conductor.idPersona = :idConductor")
    List<String> findCodigosRutaByConductorId(@Param("idConductor") Long idConductor);

    /**
     * Requerimiento Servicio 3: Consulta teniendo como parámetro la placa de un vehículo específico.
     * Visualiza el código de ruta y el conductor asociado de forma agrupada (DISTINCT).
     * CORREGIDO: Se cambió 't.persona' por 't.conductor'
     */
    @Query("SELECT DISTINCT t.codigoRuta, t.conductor FROM Trayecto t WHERE t.vehiculo.placa = :placa")
    List<Object[]> findRutaYConductorByPlaca(@Param("placa") String placa);

    /**
     * Requerimiento Servicio 4: Consulta de las rutas y la información de los trayectos donde 
     * el vehículo NO esté habilitado (en cualquiera de sus documentos) OR el conductor esté Restringido para Operar (RO).
     * CORREGIDO: Se cambió 't.persona.id' por 't.conductor.idPersona'
     */
    @Query("SELECT DISTINCT t FROM Trayecto t WHERE " +
           "EXISTS (SELECT vd FROM VehiculoDocumento vd WHERE vd.vehiculo.id = t.vehiculo.id AND vd.estado <> 'HABILITADO') " +
           "OR EXISTS (SELECT vc FROM VehiculoConductor vc WHERE vc.vehiculo.id = t.vehiculo.id AND vc.persona.id = t.conductor.idPersona AND vc.estado = 'RO - Restringido para Operar')")
    List<Trayecto> findTrayectosConRestricciones();

    /**
     * Requerimiento Técnico: Tarea programada cada 90 segundos.
     * Busca los trayectos que no contienen una longitud y latitud asociada a la ubicación registrada.
     */
    @Query("SELECT t FROM Trayecto t WHERE t.latitud IS NULL OR t.longitud IS NULL")
    List<Trayecto> findTrayectosSinCoordenadas();
}