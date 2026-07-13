package com.smartlogix.bff.dto;

import java.time.LocalDateTime;

public record PedidoResumenDTO(
        Long id,
        Long idCliente,
        String estadoPedido,
        LocalDateTime fechaCreacion,
        double total
) {
}
