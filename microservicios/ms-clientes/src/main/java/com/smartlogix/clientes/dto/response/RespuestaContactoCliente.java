package com.smartlogix.clientes.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO de salida con la información de un contacto de cliente.
 */
@Getter
@Builder
public class RespuestaContactoCliente {

    private Long idContacto;
    private Long idCliente;
    private String tipoContacto;
    private String valor;
    private Boolean esPrincipal;
}
