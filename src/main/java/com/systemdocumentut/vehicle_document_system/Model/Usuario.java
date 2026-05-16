package com.systemdocumentut.vehicle_document_system.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Usuario {
    @EmbeddedId
    private UsuarioId id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String apikey;

    @OneToOne
    @MapsId("idPersona") 
    @JoinColumn(name = "id_persona")
    private Persona persona;
}