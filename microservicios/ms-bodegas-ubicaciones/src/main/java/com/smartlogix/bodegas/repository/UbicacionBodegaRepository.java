package com.smartlogix.bodegas.repository;

import com.smartlogix.bodegas.domain.UbicacionBodega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UbicacionBodegaRepository extends JpaRepository<UbicacionBodega, Long> {
    List<UbicacionBodega> findByIdBodega(Long idBodega);
}
