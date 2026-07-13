package com.smartlogix.catalogo.service;

import com.smartlogix.catalogo.dto.response.RespuestaImagenProducto;
import com.smartlogix.catalogo.dto.request.SolicitudImagenProducto;
import com.smartlogix.catalogo.exception.ExcepcionRecursoNoEncontrado;
import com.smartlogix.catalogo.model.ImagenProducto;
import com.smartlogix.catalogo.repository.RepositorioImagenProducto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de negocio para la gestión de imágenes de productos.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServicioImagenProducto {

    private final RepositorioImagenProducto repositorioImagenProducto;
    private final ServicioProducto servicioProducto;
    private final MapeadorCatalogo mapeadorCatalogo;

    public List<RespuestaImagenProducto> listarPorProducto(Long idProducto) {
        servicioProducto.buscarEntidad(idProducto);
        return mapeadorCatalogo.aRespuestaImagenes(
                repositorioImagenProducto.findByProductoIdProductoOrderByOrdenAsc(idProducto));
    }

    @Transactional
    public RespuestaImagenProducto crear(Long idProducto, SolicitudImagenProducto solicitud) {
        ImagenProducto imagen = ImagenProducto.builder()
                .producto(servicioProducto.buscarEntidad(idProducto))
                .urlImagen(solicitud.getUrlImagen())
                .esPrincipal(solicitud.getEsPrincipal() != null ? solicitud.getEsPrincipal() : false)
                .orden(solicitud.getOrden() != null ? solicitud.getOrden() : 1)
                .build();

        return mapeadorCatalogo.aRespuestaImagen(repositorioImagenProducto.save(imagen));
    }

    @Transactional
    public void eliminar(Long idImagen) {
        ImagenProducto imagen = repositorioImagenProducto.findById(idImagen)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado(
                        "No se encontró la imagen con id: " + idImagen));
        repositorioImagenProducto.delete(imagen);
    }
}
