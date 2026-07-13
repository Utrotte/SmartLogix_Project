package com.smartlogix.pedidos.dto.inventario;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaInventarioResponse {
    private Long id;
    private Long idProducto;
    private Long idBodega;
    private Long idPedido;
    private Integer cantidadReservada;
    private String estadoReserva;
}
