package com.smartlogix.clientes.service;

import com.smartlogix.clientes.dto.response.RespuestaTipoCliente;
import com.smartlogix.clientes.dto.request.SolicitudTipoCliente;
import com.smartlogix.clientes.exception.ExcepcionNegocio;
import com.smartlogix.clientes.exception.ExcepcionRecursoNoEncontrado;
import com.smartlogix.clientes.model.TipoCliente;
import com.smartlogix.clientes.repository.RepositorioTipoCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de negocio para la gestión de tipos de cliente.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServicioTipoCliente {

    private final RepositorioTipoCliente repositorioTipoCliente;
    private final MapeadorClientes mapeadorClientes;

    public List<RespuestaTipoCliente> listarTodos() {
        return repositorioTipoCliente.findAll().stream()
                .map(mapeadorClientes::aRespuestaTipo)
                .toList();
    }

    public RespuestaTipoCliente obtenerPorId(Long idTipoCliente) {
        return mapeadorClientes.aRespuestaTipo(buscarEntidad(idTipoCliente));
    }

    @Transactional
    public RespuestaTipoCliente crear(SolicitudTipoCliente solicitud) {
        if (repositorioTipoCliente.findByNombre(solicitud.getNombre()).isPresent()) {
            throw new ExcepcionNegocio("Ya existe un tipo de cliente con el nombre: " + solicitud.getNombre());
        }

        TipoCliente tipoCliente = TipoCliente.builder()
                .nombre(solicitud.getNombre())
                .descripcion(solicitud.getDescripcion())
                .build();

        return mapeadorClientes.aRespuestaTipo(repositorioTipoCliente.save(tipoCliente));
    }

    /**
     * Obtiene la entidad JPA o lanza excepción si no existe.
     */
    public TipoCliente buscarEntidad(Long idTipoCliente) {
        return repositorioTipoCliente.findById(idTipoCliente)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado(
                        "No se encontró el tipo de cliente con id: " + idTipoCliente));
    }
}
