package com.smartlogix.bff.dto;

import java.math.BigDecimal;

public record ProductoResumenDTO(
        Long idProducto,
        String codigoSku,
        String nombre,
        String descripcion,
        String estado,
        BigDecimal precioActual
) {
}