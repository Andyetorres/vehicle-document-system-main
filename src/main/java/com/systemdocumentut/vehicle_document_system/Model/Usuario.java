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
    private UsuarioId id; // Esto contiene el login y el tipo de identificación

    private String password;
    private String apikey;

    @Column(name = "id_persona") // Esto crea la columna que te faltaba
    private Long idpersona; 
    
    // Si tienes una relación formal con la entidad Persona:
    @ManyToOne
    @JoinColumn(name = "id_persona", insertable = false, updatable = false)
    private Persona persona;
}