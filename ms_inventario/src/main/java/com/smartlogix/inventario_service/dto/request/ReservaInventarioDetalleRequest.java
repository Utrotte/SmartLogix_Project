package com.smartlogix.inventario_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaInventarioDetalleRequest {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long idProducto;

    @NotNull(message = "El ID de la bodega es obligatorio")
    private Long idBodega;

    @NotNull(message = "La cantidad reservada es obligatoria")
    @Positive(message = "La cantidad reservada debe ser mayor a 0")
    private Integer cantidadReservada;
}
