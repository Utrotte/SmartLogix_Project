package com.smartlogix.inventario_service.repository;

import com.smartlogix.inventario_service.model.Existencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExistenciaRepository extends JpaRepository<Existencia, Long> {

    Optional<Existencia> findByProductoIdProductoAndBodegaIdBodega(Long idProducto, Long idBodega);

    List<Existencia> findByProductoIdProducto(Long idProducto);

    List<Existencia> findByBodegaIdBodega(Long idBodega);
}
