package com.smartlogix.catalogo.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de salida con la información de un precio de producto.
 */
@Getter
@Builder
public class RespuestaPrecioProducto {

    private Long idPrecio;
    private Long idProducto;
    private BigDecimal precio;
    private String moneda;
    private LocalDate vigenteDesde;
    private LocalDate vigenteHasta;
    private Boolean activo;
}
