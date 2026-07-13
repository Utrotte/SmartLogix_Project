package com.smartlogix.clientes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada para crear un tipo de cliente.
 */
@Getter
@Setter
public class SolicitudTipoCliente {

    @NotBlank(message = "El nombre del tipo de cliente es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
    private String descripcion;
}
