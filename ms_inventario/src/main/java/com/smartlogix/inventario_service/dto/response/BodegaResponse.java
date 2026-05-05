package com.smartlogix.inventario_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BodegaResponse {

    private Long idBodega;

    private String nombre;

    private String direccion;

    private String comuna;

    private String ciudad;

    private String region;

    private Boolean activa;
}
