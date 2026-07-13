package com.smartlogix.catalogo.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO de salida con la información de una categoría de producto.
 */
@Getter
@Builder
public class RespuestaCategoriaProducto {

    private Long idCategoria;
    private String nombre;
    private String descripcion;
    private Boolean activo;
}
