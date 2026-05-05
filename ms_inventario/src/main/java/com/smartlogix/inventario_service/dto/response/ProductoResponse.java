package com.smartlogix.inventario_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponse {

    private Long idProducto;

    private Long idCategoria;

    private String nombreCategoria;

    private String codigoSku;

    private String nombre;

    private String descripcion;

    private String marca;

    private BigDecimal precioReferencia;

    private Boolean activo;

    private LocalDateTime fechaCreacion;
}
