package com.smartlogix.catalogo.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa una marca comercial asociada a productos.
 */
@Entity
@Table(name = "marca_producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarcaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Long idMarca;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}
