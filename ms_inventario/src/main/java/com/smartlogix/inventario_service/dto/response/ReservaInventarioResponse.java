package com.smartlogix.inventario_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaInventarioResponse {

    private Long idReserva;

    private Long idPedidoRef;

    private String codigoPedidoRef;

    private String estadoReserva;

    private LocalDateTime fechaReserva;

    private LocalDateTime fechaExpiracion;

    private List<ReservaInventarioDetalleResponse> detalles;
}
