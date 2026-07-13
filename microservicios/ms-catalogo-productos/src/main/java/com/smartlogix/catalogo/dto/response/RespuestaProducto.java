package com.smartlogix.catalogo.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de salida con la información completa de un producto del catálogo.
 */
@Getter
@Builder
public class RespuestaProducto {

    private Long idProducto;
    private Long idCategoria;
    private String nombreCategoria;
    private Long idMarca;
    private String nombreMarca;
    private String codigoSku;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private List<RespuestaPrecioProducto> precios;
    private List<RespuestaImagenProducto> imagenes;
}
