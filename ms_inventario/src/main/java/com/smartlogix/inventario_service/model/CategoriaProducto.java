package com.smartlogix.inventario_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categoria_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria; // Clave primaria

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Column(name = "nombre_categoria", nullable = false)
    private String nombreCategoria;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "activa")
    @Builder.Default
    private Boolean activa = true; // Baja lógica
}
