package com.smartlogix.inventario.repository;

import com.smartlogix.inventario.domain.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
    List<MovimientoInventario> findByIdProducto(Long idProducto);
}
