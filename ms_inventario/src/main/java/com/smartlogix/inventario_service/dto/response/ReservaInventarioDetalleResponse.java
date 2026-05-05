package com.smartlogix.inventario_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaInventarioDetalleResponse {

    private Long idReservaDetalle;

    private Long idReserva;

    private Long idProducto;

    private String codigoSkuProducto;

    private String nombreProducto;

    private Long idBodega;

    private String nombreBodega;

    private Integer cantidadReservada;

    private String estadoDetalle;
}
