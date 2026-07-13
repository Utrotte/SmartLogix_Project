package com.smartlogix.catalogo.repository;

import com.smartlogix.catalogo.model.PrecioProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio JPA para la entidad PrecioProducto.
 */
public interface RepositorioPrecioProducto extends JpaRepository<PrecioProducto, Long> {

    List<PrecioProducto> findByProductoIdProductoOrderByVigenteDesdeDesc(Long idProducto);
}
