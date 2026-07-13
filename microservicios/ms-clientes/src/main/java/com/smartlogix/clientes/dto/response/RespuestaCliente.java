package com.smartlogix.clientes.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de salida con la información de un cliente.
 */
@Getter
@Builder
public class RespuestaCliente {

    private Long idCliente;
    private Long idTipoCliente;
    private String nombreTipoCliente;
    private String nombre;
    private String apellidoRazonSocial;
    private String documento;
    private String correo;
    private String telefono;
    private String estado;
    private LocalDateTime fechaCreacion;
    private List<RespuestaDireccionCliente> direcciones;
    private List<RespuestaContactoCliente> contactos;
}
