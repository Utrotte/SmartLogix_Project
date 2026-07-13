package com.smartlogix.catalogo.service;

import com.smartlogix.catalogo.dto.response.RespuestaProducto;
import com.smartlogix.catalogo.dto.request.SolicitudProducto;
import com.smartlogix.catalogo.exception.ExcepcionNegocio;
import com.smartlogix.catalogo.exception.ExcepcionRecursoNoEncontrado;
import com.smartlogix.catalogo.model.Producto;
import com.smartlogix.catalogo.repository.RepositorioProducto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de negocio para la gestión de productos del catálogo.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServicioProducto {

    private final RepositorioProducto repositorioProducto;
    private final ServicioCategoriaProducto servicioCategoriaProducto;
    private final ServicioMarcaProducto servicioMarcaProducto;
    private final MapeadorCatalogo mapeadorCatalogo;

    public List<RespuestaProducto> listarTodos() {
    return repositorioProducto.findAll().stream()
            .map(producto -> mapeadorCatalogo.aRespuestaProducto(producto, true))
            .toList();
}

    public RespuestaProducto obtenerPorId(Long idProducto) {
        return mapeadorCatalogo.aRespuestaProducto(buscarEntidad(idProducto), true);
    }

    public RespuestaProducto obtenerPorCodigoSku(String codigoSku) {
        Producto producto = repositorioProducto.findByCodigoSku(codigoSku)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado(
                        "No se encontró el producto con SKU: " + codigoSku));
        return mapeadorCatalogo.aRespuestaProducto(producto, true);
    }

    @Transactional
    public RespuestaProducto crear(SolicitudProducto solicitud) {
        validarSkuUnico(solicitud.getCodigoSku(), null);

        Producto producto = Producto.builder()
                .categoria(servicioCategoriaProducto.buscarEntidad(solicitud.getIdCategoria()))
                .marca(servicioMarcaProducto.buscarEntidad(solicitud.getIdMarca()))
                .codigoSku(solicitud.getCodigoSku())
                .nombre(solicitud.getNombre())
                .descripcion(solicitud.getDescripcion())
                .activo(true)
                .build();

        return mapeadorCatalogo.aRespuestaProducto(repositorioProducto.save(producto), false);
    }

    @Transactional
    public RespuestaProducto actualizar(Long idProducto, SolicitudProducto solicitud) {
        validarSkuUnico(solicitud.getCodigoSku(), idProducto);

        Producto producto = buscarEntidad(idProducto);
        producto.setCategoria(servicioCategoriaProducto.buscarEntidad(solicitud.getIdCategoria()));
        producto.setMarca(servicioMarcaProducto.buscarEntidad(solicitud.getIdMarca()));
        producto.setCodigoSku(solicitud.getCodigoSku());
        producto.setNombre(solicitud.getNombre());
        producto.setDescripcion(solicitud.getDescripcion());

        return mapeadorCatalogo.aRespuestaProducto(repositorioProducto.save(producto), true);
    }

    @Transactional
    public RespuestaProducto desactivar(Long idProducto) {
        Producto producto = buscarEntidad(idProducto);
        producto.setActivo(false);
        return mapeadorCatalogo.aRespuestaProducto(repositorioProducto.save(producto), false);
    }

    /**
     * Obtiene la entidad JPA o lanza excepción si no existe.
     */
    public Producto buscarEntidad(Long idProducto) {
        return repositorioProducto.findById(idProducto)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado(
                        "No se encontró el producto con id: " + idProducto));
    }

    /**
     * Verifica que el código SKU no esté duplicado en otro producto.
     */
    private void validarSkuUnico(String codigoSku, Long idProductoExcluido) {
        boolean existeDuplicado = idProductoExcluido == null
                ? repositorioProducto.findByCodigoSku(codigoSku).isPresent()
                : repositorioProducto.existsByCodigoSkuAndIdProductoNot(codigoSku, idProductoExcluido);

        if (existeDuplicado) {
            throw new ExcepcionNegocio("Ya existe un producto con el código SKU: " + codigoSku);
        }
    }
}
