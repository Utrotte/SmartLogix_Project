package com.smartlogix.catalogo.service;

import com.smartlogix.catalogo.dto.response.*;
import com.smartlogix.catalogo.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Componente utilitario para convertir entidades JPA en DTOs de respuesta.
 */
@Component
public class MapeadorCatalogo {

    public RespuestaCategoriaProducto aRespuestaCategoria(CategoriaProducto categoria) {
        return RespuestaCategoriaProducto.builder()
                .idCategoria(categoria.getIdCategoria())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .activo(categoria.getActivo())
                .build();
    }

    public RespuestaMarcaProducto aRespuestaMarca(MarcaProducto marca) {
        return RespuestaMarcaProducto.builder()
                .idMarca(marca.getIdMarca())
                .nombre(marca.getNombre())
                .descripcion(marca.getDescripcion())
                .activo(marca.getActivo())
                .build();
    }

    public RespuestaProducto aRespuestaProducto(Producto producto, boolean incluirDetalle) {
        RespuestaProducto.RespuestaProductoBuilder constructor = RespuestaProducto.builder()
                .idProducto(producto.getIdProducto())
                .idCategoria(producto.getCategoria().getIdCategoria())
                .nombreCategoria(producto.getCategoria().getNombre())
                .idMarca(producto.getMarca().getIdMarca())
                .nombreMarca(producto.getMarca().getNombre())
                .codigoSku(producto.getCodigoSku())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .activo(producto.getActivo())
                .fechaCreacion(producto.getFechaCreacion());

        if (incluirDetalle) {
            constructor.precios(producto.getPrecios().stream().map(this::aRespuestaPrecio).toList())
                    .imagenes(producto.getImagenes().stream().map(this::aRespuestaImagen).toList());
        }

        return constructor.build();
    }

    public RespuestaPrecioProducto aRespuestaPrecio(PrecioProducto precio) {
        return RespuestaPrecioProducto.builder()
                .idPrecio(precio.getIdPrecio())
                .idProducto(precio.getProducto().getIdProducto())
                .precio(precio.getPrecio())
                .moneda(precio.getMoneda())
                .vigenteDesde(precio.getVigenteDesde())
                .vigenteHasta(precio.getVigenteHasta())
                .activo(precio.getActivo())
                .build();
    }

    public List<RespuestaPrecioProducto> aRespuestaPrecios(List<PrecioProducto> precios) {
        return precios.stream().map(this::aRespuestaPrecio).toList();
    }

    public RespuestaImagenProducto aRespuestaImagen(ImagenProducto imagen) {
        return RespuestaImagenProducto.builder()
                .idImagen(imagen.getIdImagen())
                .idProducto(imagen.getProducto().getIdProducto())
                .urlImagen(imagen.getUrlImagen())
                .esPrincipal(imagen.getEsPrincipal())
                .orden(imagen.getOrden())
                .build();
    }

    public List<RespuestaImagenProducto> aRespuestaImagenes(List<ImagenProducto> imagenes) {
        return imagenes.stream().map(this::aRespuestaImagen).toList();
    }
}
