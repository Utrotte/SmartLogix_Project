package com.smartlogix.inventario_service.repository;

import com.smartlogix.inventario_service.model.ReservaInventarioDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaInventarioDetalleRepository extends JpaRepository<ReservaInventarioDetalle, Long> {
}
