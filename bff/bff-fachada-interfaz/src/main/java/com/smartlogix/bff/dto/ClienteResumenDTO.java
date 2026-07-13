package com.smartlogix.bff.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ClienteResumenDTO(
        Long idCliente,
        Long idTipoCliente,
        String nombreTipoCliente,
        String nombre,
        String apellidoRazonSocial,
        String documento,
        String correo,
        String telefono,
        String estado,
        LocalDateTime fechaCreacion,
        List<Object> direcciones,
        List<Object> contactos
) {
}