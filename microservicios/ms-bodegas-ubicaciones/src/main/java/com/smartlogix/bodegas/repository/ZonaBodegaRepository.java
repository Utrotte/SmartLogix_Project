package com.smartlogix.bodegas.repository;

import com.smartlogix.bodegas.domain.ZonaBodega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZonaBodegaRepository extends JpaRepository<ZonaBodega, Long> {
    List<ZonaBodega> findByIdBodega(Long idBodega);
}
