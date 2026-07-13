package com.smartlogix.catalogo.repository;

import com.smartlogix.catalogo.model.ImagenProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio JPA para la entidad ImagenProducto.
 */
public interface RepositorioImagenProducto extends JpaRepository<ImagenProducto, Long> {

    List<ImagenProducto> findByProductoIdProductoOrderByOrdenAsc(Long idProducto);
}
