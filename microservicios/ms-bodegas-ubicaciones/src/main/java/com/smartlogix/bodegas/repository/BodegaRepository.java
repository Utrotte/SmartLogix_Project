package com.smartlogix.bodegas.repository;

import com.smartlogix.bodegas.domain.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BodegaRepository extends JpaRepository<Bodega, Long> {
}
