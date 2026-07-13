package com.smartlogix.catalogo.service;

import com.smartlogix.catalogo.dto.response.RespuestaPrecioProducto;
import com.smartlogix.catalogo.dto.request.SolicitudPrecioProducto;
import com.smartlogix.catalogo.exception.ExcepcionNegocio;
import com.smartlogix.catalogo.exception.ExcepcionRecursoNoEncontrado;
import com.smartlogix.catalogo.model.PrecioProducto;
import com.smartlogix.catalogo.repository.RepositorioPrecioProducto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de negocio para la gestión de precios de productos.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServicioPrecioProducto {

    private final RepositorioPrecioProducto repositorioPrecioProducto;
    private final ServicioProducto servicioProducto;
    private final MapeadorCatalogo mapeadorCatalogo;

    public List<RespuestaPrecioProducto> listarPorProducto(Long idProducto) {
        servicioProducto.buscarEntidad(idProducto);
        return mapeadorCatalogo.aRespuestaPrecios(
                repositorioPrecioProducto.findByProductoIdProductoOrderByVigenteDesdeDesc(idProducto));
    }

    @Transactional
    public RespuestaPrecioProducto crear(Long idProducto, SolicitudPrecioProducto solicitud) {
        validarRangoFechas(solicitud);

        PrecioProducto precio = PrecioProducto.builder()
                .producto(servicioProducto.buscarEntidad(idProducto))
                .precio(solicitud.getPrecio())
                .moneda(solicitud.getMoneda())
                .vigenteDesde(solicitud.getVigenteDesde())
                .vigenteHasta(solicitud.getVigenteHasta())
                .activo(true)
                .build();

        return mapeadorCatalogo.aRespuestaPrecio(repositorioPrecioProducto.save(precio));
    }

    @Transactional
    public RespuestaPrecioProducto desactivar(Long idPrecio) {
        PrecioProducto precio = repositorioPrecioProducto.findById(idPrecio)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado(
                        "No se encontró el precio con id: " + idPrecio));
        precio.setActivo(false);
        return mapeadorCatalogo.aRespuestaPrecio(repositorioPrecioProducto.save(precio));
    }

    /**
     * Valida que la fecha de término sea posterior a la de inicio cuando ambas están presentes.
     */
    private void validarRangoFechas(SolicitudPrecioProducto solicitud) {
        if (solicitud.getVigenteHasta() != null
                && solicitud.getVigenteHasta().isBefore(solicitud.getVigenteDesde())) {
            throw new ExcepcionNegocio("La fecha de término de vigencia debe ser posterior a la de inicio");
        }
    }
}
