package com.smartlogix.inventario_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AjustarStockRequest {

    // idExistencia viene como @PathVariable en la URL, no en el body
    // NO incluir aquí para evitar error de validación cuando el frontend
    // no lo envíe en el body

    @NotNull(message = "El ajuste es obligatorio")
    private Integer ajuste;

    private String observacion;
}
