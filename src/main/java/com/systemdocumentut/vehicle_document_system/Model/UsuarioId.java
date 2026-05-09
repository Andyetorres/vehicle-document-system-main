package com.systemdocumentut.vehicle_document_system.Model;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioId implements Serializable {
    private String login;
    private Long idpersona;
}