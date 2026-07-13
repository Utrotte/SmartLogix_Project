package com.smartlogix.clientes.service;

import com.smartlogix.clientes.dto.response.RespuestaContactoCliente;
import com.smartlogix.clientes.dto.request.SolicitudContactoCliente;
import com.smartlogix.clientes.exception.ExcepcionRecursoNoEncontrado;
import com.smartlogix.clientes.model.ContactoCliente;
import com.smartlogix.clientes.repository.RepositorioContactoCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de negocio para la gestión de contactos de clientes.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServicioContactoCliente {

    private final RepositorioContactoCliente repositorioContactoCliente;
    private final ServicioCliente servicioCliente;
    private final MapeadorClientes mapeadorClientes;

    public List<RespuestaContactoCliente> listarPorCliente(Long idCliente) {
        servicioCliente.buscarEntidad(idCliente);
        return mapeadorClientes.aRespuestaContactos(
                repositorioContactoCliente.findByClienteIdCliente(idCliente));
    }

    @Transactional
    public RespuestaContactoCliente crear(Long idCliente, SolicitudContactoCliente solicitud) {
        ContactoCliente contacto = ContactoCliente.builder()
                .cliente(servicioCliente.buscarEntidad(idCliente))
                .tipoContacto(solicitud.getTipoContacto())
                .valor(solicitud.getValor())
                .esPrincipal(solicitud.getEsPrincipal() != null ? solicitud.getEsPrincipal() : false)
                .build();

        return mapeadorClientes.aRespuestaContacto(repositorioContactoCliente.save(contacto));
    }

    @Transactional
    public RespuestaContactoCliente actualizar(Long idContacto, SolicitudContactoCliente solicitud) {
        ContactoCliente contacto = buscarEntidad(idContacto);
        contacto.setTipoContacto(solicitud.getTipoContacto());
        contacto.setValor(solicitud.getValor());
        contacto.setEsPrincipal(solicitud.getEsPrincipal() != null ? solicitud.getEsPrincipal() : false);

        return mapeadorClientes.aRespuestaContacto(repositorioContactoCliente.save(contacto));
    }

    /**
     * Obtiene la entidad JPA o lanza excepción si no existe.
     */
    public ContactoCliente buscarEntidad(Long idContacto) {
        return repositorioContactoCliente.findById(idContacto)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado(
                        "No se encontró el contacto con id: " + idContacto));
    }
}
