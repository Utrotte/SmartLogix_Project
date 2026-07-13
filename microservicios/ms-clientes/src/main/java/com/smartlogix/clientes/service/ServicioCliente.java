package com.smartlogix.clientes.service;

import com.smartlogix.clientes.dto.response.RespuestaCliente;
import com.smartlogix.clientes.dto.request.SolicitudCliente;
import com.smartlogix.clientes.exception.ExcepcionNegocio;
import com.smartlogix.clientes.exception.ExcepcionRecursoNoEncontrado;
import com.smartlogix.clientes.model.Cliente;
import com.smartlogix.clientes.repository.RepositorioCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Servicio de negocio para la gestión de clientes.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServicioCliente {

    private final RepositorioCliente repositorioCliente;
    private final ServicioTipoCliente servicioTipoCliente;
    private final MapeadorClientes mapeadorClientes;

    public List<RespuestaCliente> listarTodos() {
        return repositorioCliente.findAll().stream()
                .map(cliente -> mapeadorClientes.aRespuestaCliente(cliente, false))
                .toList();
    }

    public RespuestaCliente obtenerPorId(Long idCliente) {
        return mapeadorClientes.aRespuestaCliente(buscarEntidad(idCliente), true);
    }

    @Transactional
    public RespuestaCliente crear(SolicitudCliente solicitud) {
        validarUnicidad(solicitud.getDocumento(), solicitud.getCorreo(), null);

        Cliente cliente = Cliente.builder()
                .tipoCliente(servicioTipoCliente.buscarEntidad(solicitud.getIdTipoCliente()))
                .nombre(solicitud.getNombre())
                .apellidoRazonSocial(solicitud.getApellidoRazonSocial())
                .documento(solicitud.getDocumento())
                .correo(solicitud.getCorreo())
                .telefono(solicitud.getTelefono())
                .estado("ACTIVO")
                .build();

        return mapeadorClientes.aRespuestaCliente(repositorioCliente.save(cliente), false);
    }

    @Transactional
    public RespuestaCliente actualizar(Long idCliente, SolicitudCliente solicitud) {
        validarUnicidad(solicitud.getDocumento(), solicitud.getCorreo(), idCliente);

        Cliente cliente = buscarEntidad(idCliente);
        cliente.setTipoCliente(servicioTipoCliente.buscarEntidad(solicitud.getIdTipoCliente()));
        cliente.setNombre(solicitud.getNombre());
        cliente.setApellidoRazonSocial(solicitud.getApellidoRazonSocial());
        cliente.setDocumento(solicitud.getDocumento());
        cliente.setCorreo(solicitud.getCorreo());
        cliente.setTelefono(solicitud.getTelefono());

        return mapeadorClientes.aRespuestaCliente(repositorioCliente.save(cliente), true);
    }

    @Transactional
    public RespuestaCliente desactivar(Long idCliente) {
        Cliente cliente = buscarEntidad(idCliente);
        cliente.setEstado("INACTIVO");
        return mapeadorClientes.aRespuestaCliente(repositorioCliente.save(cliente), false);
    }

    /**
     * Obtiene la entidad JPA o lanza excepción si no existe.
     */
    public Cliente buscarEntidad(Long idCliente) {
        return repositorioCliente.findById(idCliente)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado(
                        "No se encontró el cliente con id: " + idCliente));
    }

    /**
     * Verifica que documento y correo no estén duplicados en otro cliente.
     */
    private void validarUnicidad(String documento, String correo, Long idClienteExcluido) {
        if (StringUtils.hasText(documento)) {
            boolean documentoDuplicado = idClienteExcluido == null
                    ? repositorioCliente.findByDocumento(documento).isPresent()
                    : repositorioCliente.existsByDocumentoAndIdClienteNot(documento, idClienteExcluido);

            if (documentoDuplicado) {
                throw new ExcepcionNegocio("Ya existe un cliente con el documento: " + documento);
            }
        }

        if (StringUtils.hasText(correo)) {
            boolean correoDuplicado = idClienteExcluido == null
                    ? repositorioCliente.findByCorreo(correo).isPresent()
                    : repositorioCliente.existsByCorreoAndIdClienteNot(correo, idClienteExcluido);

            if (correoDuplicado) {
                throw new ExcepcionNegocio("Ya existe un cliente con el correo: " + correo);
            }
        }
    }
}
