package com.smartlogix.clientes.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO de salida con la información de un tipo de cliente.
 */
@Getter
@Builder
public class RespuestaTipoCliente {

    private Long idTipoCliente;
    private String nombre;
    private String descripcion;
}
