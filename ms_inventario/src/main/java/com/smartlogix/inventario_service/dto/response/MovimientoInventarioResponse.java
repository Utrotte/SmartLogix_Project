package com.smartlogix.inventario_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoInventarioResponse {

    private Long idMovimiento;

    private Long idProducto;

    private String codigoSkuProducto;

    private String nombreProducto;

    private Long idBodega;

    private String nombreBodega;

    private Long idReserva;

    private String tipoMovimiento;

    private Integer cantidad;

    private Integer stockResultante;

    private String referencia;

    private LocalDateTime fechaMovimiento;

    private String observacion;
}
