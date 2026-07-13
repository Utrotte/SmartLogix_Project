package com.smartlogix.catalogo.repository;

import com.smartlogix.catalogo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Producto.
 */
public interface RepositorioProducto extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigoSku(String codigoSku);

    boolean existsByCodigoSkuAndIdProductoNot(String codigoSku, Long idProducto);
}
