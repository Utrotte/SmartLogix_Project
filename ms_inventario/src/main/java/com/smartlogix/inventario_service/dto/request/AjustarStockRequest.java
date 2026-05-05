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
public class AjustarStockRequest {

    @NotNull(message = "El ID del existencia es obligatorio")
    private Long idExistencia;

    @NotNull(message = "El ajuste es obligatorio")
    @Positive(message = "El ajuste debe ser un valor entero positivo o negativo (sin signo en el campo)")
    private Integer ajuste;

    private String observacion;
}
