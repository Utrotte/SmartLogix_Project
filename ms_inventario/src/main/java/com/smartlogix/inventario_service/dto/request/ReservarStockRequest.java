package com.smartlogix.inventario_service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class ReservarStockRequest {

    @NotNull(message = "El ID del pedido es obligatorio")
    @Positive(message = "El ID del pedido debe ser positivo")
    private Long idPedidoRef;

    @NotBlank(message = "El código del pedido es obligatorio")
    private String codigoPedidoRef;

    private LocalDateTime fechaExpiracion;

    @NotNull(message = "Los detalles de la reserva son obligatorios")
    @Valid
    private List<ReservaInventarioDetalleRequest> detalles;
}
