package com.smartlogix.inventario_service.repository;

import com.smartlogix.inventario_service.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigoSku(String codigoSku);

    boolean existsByCodigoSku(String codigoSku);
}
