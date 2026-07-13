package com.smartlogix.catalogo.service;

import com.smartlogix.catalogo.dto.response.RespuestaMarcaProducto;
import com.smartlogix.catalogo.dto.request.SolicitudMarcaProducto;
import com.smartlogix.catalogo.exception.ExcepcionRecursoNoEncontrado;
import com.smartlogix.catalogo.model.MarcaProducto;
import com.smartlogix.catalogo.repository.RepositorioMarcaProducto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de negocio para la gestión de marcas de productos.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServicioMarcaProducto {

    private final RepositorioMarcaProducto repositorioMarcaProducto;
    private final MapeadorCatalogo mapeadorCatalogo;

    public List<RespuestaMarcaProducto> listarTodas() {
        return repositorioMarcaProducto.findAll().stream()
                .map(mapeadorCatalogo::aRespuestaMarca)
                .toList();
    }

    public RespuestaMarcaProducto obtenerPorId(Long idMarca) {
        return mapeadorCatalogo.aRespuestaMarca(buscarEntidad(idMarca));
    }

    @Transactional
    public RespuestaMarcaProducto crear(SolicitudMarcaProducto solicitud) {
        MarcaProducto marca = MarcaProducto.builder()
                .nombre(solicitud.getNombre())
                .descripcion(solicitud.getDescripcion())
                .activo(true)
                .build();
        return mapeadorCatalogo.aRespuestaMarca(repositorioMarcaProducto.save(marca));
    }

    @Transactional
    public RespuestaMarcaProducto actualizar(Long idMarca, SolicitudMarcaProducto solicitud) {
        MarcaProducto marca = buscarEntidad(idMarca);
        marca.setNombre(solicitud.getNombre());
        marca.setDescripcion(solicitud.getDescripcion());
        return mapeadorCatalogo.aRespuestaMarca(repositorioMarcaProducto.save(marca));
    }

    @Transactional
    public RespuestaMarcaProducto desactivar(Long idMarca) {
        MarcaProducto marca = buscarEntidad(idMarca);
        marca.setActivo(false);
        return mapeadorCatalogo.aRespuestaMarca(repositorioMarcaProducto.save(marca));
    }

    /**
     * Obtiene la entidad JPA o lanza excepción si no existe.
     */
    public MarcaProducto buscarEntidad(Long idMarca) {
        return repositorioMarcaProducto.findById(idMarca)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado(
                        "No se encontró la marca con id: " + idMarca));
    }
}
