package com.smartlogix.catalogo.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO de salida con la información de una imagen de producto.
 */
@Getter
@Builder
public class RespuestaImagenProducto {

    private Long idImagen;
    private Long idProducto;
    private String urlImagen;
    private Boolean esPrincipal;
    private Integer orden;
}
