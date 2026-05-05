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
public class ExistenciaResponse {

    private Long idExistencia;

    private Long idProducto;

    private String codigoSkuProducto;

    private String nombreProducto;

    private Long idBodega;

    private String nombreBodega;

    private Integer stockActual;

    private Integer stockReservado;

    private Integer stockDisponible;

    private Integer stockMinimo;

    private LocalDateTime fechaActualizacion;
}
