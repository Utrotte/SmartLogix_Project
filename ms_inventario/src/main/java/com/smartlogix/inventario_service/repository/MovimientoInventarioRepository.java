package com.smartlogix.inventario_service.repository;

import com.smartlogix.inventario_service.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findByProductoIdProducto(Long idProducto);

    List<MovimientoInventario> findByBodegaIdBodega(Long idBodega);
}
