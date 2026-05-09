    package com.systemdocumentut.vehicle_document_system.DTOs;

    import lombok.*;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public class PersonaDTO {
        private Long id;
        private String identificacion;
        private String tipoIdentificacion; // CC
        private String nombres;
        private String apellidos;
        private String correoElectronico;
        private String tipoPersona; // C o A
    }