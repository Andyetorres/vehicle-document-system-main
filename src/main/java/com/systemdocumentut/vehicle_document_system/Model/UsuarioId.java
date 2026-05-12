package com.systemdocumentut.vehicle_document_system.Model;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioId implements Serializable {
    private String login;
    private Long idPersona; // Este nombre debe coincidir con el atributo en Usuario
}