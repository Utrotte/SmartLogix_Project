package com.smartlogix.clientes.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada para crear o actualizar un cliente.
 */
@Getter
@Setter
public class SolicitudCliente {

    @NotNull(message = "El identificador del tipo de cliente es obligatorio")
    private Long idTipoCliente;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
    private String nombre;

    @Size(max = 150, message = "El apellido o razón social no puede superar 150 caracteres")
    private String apellidoRazonSocial;

    @Size(max = 30, message = "El documento no puede superar 30 caracteres")
    private String documento;

    @Email(message = "El correo electrónico no tiene un formato válido")
    @Size(max = 150, message = "El correo no puede superar 150 caracteres")
    private String correo;

    @Size(max = 30, message = "El teléfono no puede superar 30 caracteres")
    private String telefono;
}
