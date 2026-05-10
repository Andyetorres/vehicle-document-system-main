package com.systemdocumentut.vehicle_document_system.Model;

import jakarta.persistence.Column; // Asegúrate de importar esto
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coordenadas")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class Coordenadas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coordenada") // Especificamos el nombre exacto de la DB
    private Integer id_coordenada;
    
    @Column(name = "persona")
    private Integer persona; 
    
    @Column(name = "me_marca")
    private String me_marca; 
    
    @Column(name = "latitud") // Obligamos a JPA a buscar esta columna
    private Double latitud;
    
    @Column(name = "longitud") // Obligamos a JPA a buscar esta columna
    private Double longitud;
}