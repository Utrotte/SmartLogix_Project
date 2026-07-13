package com.smartlogix.catalogo.service;

import com.smartlogix.catalogo.dto.response.RespuestaCategoriaProducto;
import com.smartlogix.catalogo.dto.request.SolicitudCategoriaProducto;
import com.smartlogix.catalogo.exception.ExcepcionRecursoNoEncontrado;
import com.smartlogix.catalogo.model.CategoriaProducto;
import com.smartlogix.catalogo.repository.RepositorioCategoriaProducto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de negocio para la gestión de categorías de productos.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServicioCategoriaProducto {

    private final RepositorioCategoriaProducto repositorioCategoriaProducto;
    private final MapeadorCatalogo mapeadorCatalogo;

    public List<RespuestaCategoriaProducto> listarTodas() {
        return repositorioCategoriaProducto.findAll().stream()
                .map(mapeadorCatalogo::aRespuestaCategoria)
                .toList();
    }

    public RespuestaCategoriaProducto obtenerPorId(Long idCategoria) {
        return mapeadorCatalogo.aRespuestaCategoria(buscarEntidad(idCategoria));
    }

    @Transactional
    public RespuestaCategoriaProducto crear(SolicitudCategoriaProducto solicitud) {
        CategoriaProducto categoria = CategoriaProducto.builder()
                .nombre(solicitud.getNombre())
                .descripcion(solicitud.getDescripcion())
                .activo(true)
                .build();
        return mapeadorCatalogo.aRespuestaCategoria(repositorioCategoriaProducto.save(categoria));
    }

    @Transactional
    public RespuestaCategoriaProducto actualizar(Long idCategoria, SolicitudCategoriaProducto solicitud) {
        CategoriaProducto categoria = buscarEntidad(idCategoria);
        categoria.setNombre(solicitud.getNombre());
        categoria.setDescripcion(solicitud.getDescripcion());
        return mapeadorCatalogo.aRespuestaCategoria(repositorioCategoriaProducto.save(categoria));
    }

    @Transactional
    public RespuestaCategoriaProducto desactivar(Long idCategoria) {
        CategoriaProducto categoria = buscarEntidad(idCategoria);
        categoria.setActivo(false);
        return mapeadorCatalogo.aRespuestaCategoria(repositorioCategoriaProducto.save(categoria));
    }

    /**
     * Obtiene la entidad JPA o lanza excepción si no existe.
     */
    public CategoriaProducto buscarEntidad(Long idCategoria) {
        return repositorioCategoriaProducto.findById(idCategoria)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado(
                        "No se encontró la categoría con id: " + idCategoria));
    }
}
