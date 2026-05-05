package com.smartlogix.inventario_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaProductoResponse {

    private Long idCategoria;

    private String nombreCategoria;

    private String descripcion;

    private Boolean activa;
}
