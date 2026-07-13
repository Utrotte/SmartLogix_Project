package com.smartlogix.pedidos.dto;

import com.smartlogix.pedidos.domain.EstadoPedido;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarEstadoPedidoRequest {
    @NotNull
    private EstadoPedido estadoPedido;
}
