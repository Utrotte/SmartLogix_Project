package com.smartlogix.clientes.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO de salida con la información de una dirección de cliente.
 */
@Getter
@Builder
public class RespuestaDireccionCliente {

    private Long idDireccion;
    private Long idCliente;
    private String tipoDireccion;
    private String calle;
    private String numero;
    private String comuna;
    private String ciudad;
    private String region;
    private String codigoPostal;
    private Boolean esPrincipal;
}
