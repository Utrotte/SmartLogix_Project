package com.smartlogix.clientes.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada para crear o actualizar una dirección de cliente.
 */
@Getter
@Setter
public class SolicitudDireccionCliente {

    @Size(max = 50, message = "El tipo de dirección no puede superar 50 caracteres")
    private String tipoDireccion;

    @Size(max = 150, message = "La calle no puede superar 150 caracteres")
    private String calle;

    @Size(max = 20, message = "El número no puede superar 20 caracteres")
    private String numero;

    @Size(max = 100, message = "La comuna no puede superar 100 caracteres")
    private String comuna;

    @Size(max = 100, message = "La ciudad no puede superar 100 caracteres")
    private String ciudad;

    @Size(max = 100, message = "La región no puede superar 100 caracteres")
    private String region;

    @Size(max = 20, message = "El código postal no puede superar 20 caracteres")
    private String codigoPostal;

    private Boolean esPrincipal;
}
