package com.smartlogix.pedidos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearPedidoRequest {

    @NotNull
    private Long idCliente;

    @NotEmpty
    @Valid
    private List<CrearDetallePedidoRequest> detalles;

}
