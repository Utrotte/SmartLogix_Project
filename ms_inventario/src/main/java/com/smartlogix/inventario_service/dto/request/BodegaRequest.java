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
public class BodegaRequest {

    @NotBlank(message = "El nombre de la bodega es obligatorio")
    private String nombre;

    private String direccion;

    private String comuna;

    private String ciudad;

    private String region;

    private Boolean activa;
}
