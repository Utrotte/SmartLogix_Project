package com.smartlogix.clientes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada para crear o actualizar un contacto de cliente.
 */
@Getter
@Setter
public class SolicitudContactoCliente {

    @NotBlank(message = "El tipo de contacto es obligatorio")
    @Size(max = 50, message = "El tipo de contacto no puede superar 50 caracteres")
    private String tipoContacto;

    @NotBlank(message = "El valor del contacto es obligatorio")
    @Size(max = 150, message = "El valor del contacto no puede superar 150 caracteres")
    private String valor;

    private Boolean esPrincipal;
}
