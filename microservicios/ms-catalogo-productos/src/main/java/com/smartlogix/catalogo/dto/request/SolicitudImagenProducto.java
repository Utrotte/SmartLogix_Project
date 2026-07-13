package com.smartlogix.catalogo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada para agregar una imagen a un producto.
 */
@Getter
@Setter
public class SolicitudImagenProducto {

    @NotBlank(message = "La URL de la imagen es obligatoria")
    @Size(max = 500, message = "La URL no puede superar 500 caracteres")
    private String urlImagen;

    private Boolean esPrincipal;

    @Min(value = 1, message = "El orden debe ser al menos 1")
    private Integer orden;
}
