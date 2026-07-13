package com.smartlogix.catalogo.repository;

import com.smartlogix.catalogo.model.MarcaProducto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad MarcaProducto.
 */
public interface RepositorioMarcaProducto extends JpaRepository<MarcaProducto, Long> {
}
