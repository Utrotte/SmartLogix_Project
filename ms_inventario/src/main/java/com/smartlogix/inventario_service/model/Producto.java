package com.smartlogix.inventario_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto; // Clave primaria

    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaProducto categoria; // Relación con categoría

    @NotBlank(message = "El código SKU es obligatorio")
    @Column(name = "codigo_sku", nullable = false, unique = true)
    private String codigoSku; // Identificador único del producto

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "marca")
    private String marca;

    @DecimalMin(value = "0", inclusive = true, message = "El precio de referencia debe ser mayor o igual a 0")
    @Column(name = "precio_referencia")
    private BigDecimal precioReferencia;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true; // Baja lógica

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion; // Timestamp de creación

    // Asigna la fecha de creación automáticamente antes de insertar
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
