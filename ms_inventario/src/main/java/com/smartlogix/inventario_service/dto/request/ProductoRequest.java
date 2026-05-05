package com.smartlogix.inventario_service.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoRequest {

    @NotNull(message = "El ID de la categoría es obligatorio")
    private Long idCategoria;

    @NotBlank(message = "El código SKU es obligatorio")
    private String codigoSku;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    private String descripcion;

    private String marca;

    @DecimalMin(value = "0", inclusive = true, message = "El precio debe ser mayor o igual a 0")
    private BigDecimal precioReferencia;

    private Boolean activo;
}
