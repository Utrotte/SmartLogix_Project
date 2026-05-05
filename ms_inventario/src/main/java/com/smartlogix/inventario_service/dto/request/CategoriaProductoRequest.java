package com.smartlogix.inventario_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaProductoRequest {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombreCategoria;

    private String descripcion;

    private Boolean activa;
}
