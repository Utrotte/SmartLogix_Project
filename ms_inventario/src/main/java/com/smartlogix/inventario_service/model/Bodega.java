package com.smartlogix.inventario_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bodega")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bodega")
    private Long idBodega; // Clave primaria

    @NotBlank(message = "El nombre de la bodega es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "comuna")
    private String comuna;

    @Column(name = "ciudad")
    private String ciudad;

    @Column(name = "region")
    private String region;

    @Column(name = "activa")
    @Builder.Default
    private Boolean activa = true; // Baja lógica: true = activa, false = inactiva
}
