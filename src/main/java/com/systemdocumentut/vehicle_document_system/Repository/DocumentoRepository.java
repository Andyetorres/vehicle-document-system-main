package com.systemdocumentut.vehicle_document_system.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systemdocumentut.vehicle_document_system.Model.Documento;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
}