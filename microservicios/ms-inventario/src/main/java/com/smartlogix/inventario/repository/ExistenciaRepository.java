package com.smartlogix.inventario.repository;

import com.smartlogix.inventario.domain.Existencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExistenciaRepository extends JpaRepository<Existencia, Long> {
    List<Existencia> findByIdProducto(Long idProducto);
    List<Existencia> findByIdBodega(Long idBodega);
    Existencia findByIdProductoAndIdBodega(Long idProducto, Long idBodega);
}
