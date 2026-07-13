package com.smartlogix.catalogo.repository;

import com.smartlogix.catalogo.model.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad CategoriaProducto.
 */
public interface RepositorioCategoriaProducto extends JpaRepository<CategoriaProducto, Long> {
}
