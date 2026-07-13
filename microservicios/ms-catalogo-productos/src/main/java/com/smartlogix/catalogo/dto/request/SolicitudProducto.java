package com.smartlogix.catalogo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada para crear o actualizar un producto del catálogo.
 */
@Getter
@Setter
public class SolicitudProducto {

    @NotNull(message = "El identificador de categoría es obligatorio")
    private Long idCategoria;

    @NotNull(message = "El identificador de marca es obligatorio")
    private Long idMarca;

    @NotBlank(message = "El código SKU es obligatorio")
    @Size(max = 50, message = "El código SKU no puede superar 50 caracteres")
    private String codigoSku;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 200, message = "El nombre no puede superar 200 caracteres")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede superar 1000 caracteres")
    private String descripcion;
}
