package com.smartlogix.clientes.service;

import com.smartlogix.clientes.dto.response.*;
import com.smartlogix.clientes.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Componente utilitario para convertir entidades JPA en DTOs de respuesta.
 */
@Component
public class MapeadorClientes {

    public RespuestaTipoCliente aRespuestaTipo(TipoCliente tipoCliente) {
        return RespuestaTipoCliente.builder()
                .idTipoCliente(tipoCliente.getIdTipoCliente())
                .nombre(tipoCliente.getNombre())
                .descripcion(tipoCliente.getDescripcion())
                .build();
    }

    public RespuestaCliente aRespuestaCliente(Cliente cliente, boolean incluirDetalle) {
        RespuestaCliente.RespuestaClienteBuilder constructor = RespuestaCliente.builder()
                .idCliente(cliente.getIdCliente())
                .idTipoCliente(cliente.getTipoCliente().getIdTipoCliente())
                .nombreTipoCliente(cliente.getTipoCliente().getNombre())
                .nombre(cliente.getNombre())
                .apellidoRazonSocial(cliente.getApellidoRazonSocial())
                .documento(cliente.getDocumento())
                .correo(cliente.getCorreo())
                .telefono(cliente.getTelefono())
                .estado(cliente.getEstado())
                .fechaCreacion(cliente.getFechaCreacion());

        if (incluirDetalle) {
            constructor.direcciones(cliente.getDirecciones().stream().map(this::aRespuestaDireccion).toList())
                    .contactos(cliente.getContactos().stream().map(this::aRespuestaContacto).toList());
        }

        return constructor.build();
    }

    public RespuestaDireccionCliente aRespuestaDireccion(DireccionCliente direccion) {
        return RespuestaDireccionCliente.builder()
                .idDireccion(direccion.getIdDireccion())
                .idCliente(direccion.getCliente().getIdCliente())
                .tipoDireccion(direccion.getTipoDireccion())
                .calle(direccion.getCalle())
                .numero(direccion.getNumero())
                .comuna(direccion.getComuna())
                .ciudad(direccion.getCiudad())
                .region(direccion.getRegion())
                .codigoPostal(direccion.getCodigoPostal())
                .esPrincipal(direccion.getEsPrincipal())
                .build();
    }

    public List<RespuestaDireccionCliente> aRespuestaDirecciones(List<DireccionCliente> direcciones) {
        return direcciones.stream().map(this::aRespuestaDireccion).toList();
    }

    public RespuestaContactoCliente aRespuestaContacto(ContactoCliente contacto) {
        return RespuestaContactoCliente.builder()
                .idContacto(contacto.getIdContacto())
                .idCliente(contacto.getCliente().getIdCliente())
                .tipoContacto(contacto.getTipoContacto())
                .valor(contacto.getValor())
                .esPrincipal(contacto.getEsPrincipal())
                .build();
    }

    public List<RespuestaContactoCliente> aRespuestaContactos(List<ContactoCliente> contactos) {
        return contactos.stream().map(this::aRespuestaContacto).toList();
    }
}
