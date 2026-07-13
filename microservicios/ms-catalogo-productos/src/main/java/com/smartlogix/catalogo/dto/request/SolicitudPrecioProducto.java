package com.smartlogix.catalogo.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de entrada para registrar un precio asociado a un producto.
 */
@Getter
@Setter
public class SolicitudPrecioProducto {

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
    private BigDecimal precio;

    @NotBlank(message = "La moneda es obligatoria")
    @Size(min = 3, max = 3, message = "La moneda debe tener 3 caracteres (ej: CLP)")
    private String moneda;

    @NotNull(message = "La fecha de inicio de vigencia es obligatoria")
    private LocalDate vigenteDesde;

    private LocalDate vigenteHasta;
}
