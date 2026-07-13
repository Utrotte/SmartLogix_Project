package com.smartlogix.pedidos.dto;

import com.smartlogix.pedidos.domain.EstadoPedido;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponse {
    private Long id;
    private Long idCliente;
    private EstadoPedido estadoPedido;
    private BigDecimal total;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private boolean activo;
    private List<DetallePedidoResponse> detalles;
}
