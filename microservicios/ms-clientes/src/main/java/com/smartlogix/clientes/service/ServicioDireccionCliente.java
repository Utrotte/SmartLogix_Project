package com.smartlogix.clientes.service;

import com.smartlogix.clientes.dto.response.RespuestaDireccionCliente;
import com.smartlogix.clientes.dto.request.SolicitudDireccionCliente;
import com.smartlogix.clientes.exception.ExcepcionRecursoNoEncontrado;
import com.smartlogix.clientes.model.DireccionCliente;
import com.smartlogix.clientes.repository.RepositorioDireccionCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de negocio para la gestión de direcciones de clientes.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServicioDireccionCliente {

    private final RepositorioDireccionCliente repositorioDireccionCliente;
    private final ServicioCliente servicioCliente;
    private final MapeadorClientes mapeadorClientes;

    public List<RespuestaDireccionCliente> listarPorCliente(Long idCliente) {
        servicioCliente.buscarEntidad(idCliente);
        return mapeadorClientes.aRespuestaDirecciones(
                repositorioDireccionCliente.findByClienteIdCliente(idCliente));
    }

    @Transactional
    public RespuestaDireccionCliente crear(Long idCliente, SolicitudDireccionCliente solicitud) {
        DireccionCliente direccion = DireccionCliente.builder()
                .cliente(servicioCliente.buscarEntidad(idCliente))
                .tipoDireccion(solicitud.getTipoDireccion())
                .calle(solicitud.getCalle())
                .numero(solicitud.getNumero())
                .comuna(solicitud.getComuna())
                .ciudad(solicitud.getCiudad())
                .region(solicitud.getRegion())
                .codigoPostal(solicitud.getCodigoPostal())
                .esPrincipal(solicitud.getEsPrincipal() != null ? solicitud.getEsPrincipal() : false)
                .build();

        return mapeadorClientes.aRespuestaDireccion(repositorioDireccionCliente.save(direccion));
    }

    @Transactional
    public RespuestaDireccionCliente actualizar(Long idDireccion, SolicitudDireccionCliente solicitud) {
        DireccionCliente direccion = buscarEntidad(idDireccion);
        direccion.setTipoDireccion(solicitud.getTipoDireccion());
        direccion.setCalle(solicitud.getCalle());
        direccion.setNumero(solicitud.getNumero());
        direccion.setComuna(solicitud.getComuna());
        direccion.setCiudad(solicitud.getCiudad());
        direccion.setRegion(solicitud.getRegion());
        direccion.setCodigoPostal(solicitud.getCodigoPostal());
        direccion.setEsPrincipal(solicitud.getEsPrincipal() != null ? solicitud.getEsPrincipal() : false);

        return mapeadorClientes.aRespuestaDireccion(repositorioDireccionCliente.save(direccion));
    }

    /**
     * Obtiene la entidad JPA o lanza excepción si no existe.
     */
    public DireccionCliente buscarEntidad(Long idDireccion) {
        return repositorioDireccionCliente.findById(idDireccion)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado(
                        "No se encontró la dirección con id: " + idDireccion));
    }
}
