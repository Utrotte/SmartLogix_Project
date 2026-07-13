package com.smartlogix.catalogo.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO de salida con la información de una marca de producto.
 */
@Getter
@Builder
public class RespuestaMarcaProducto {

    private Long idMarca;
    private String nombre;
    private String descripcion;
    private Boolean activo;
}
