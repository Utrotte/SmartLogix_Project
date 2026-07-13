package com.smartlogix.inventario.repository;

import com.smartlogix.inventario.domain.ReservaInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaInventarioRepository extends JpaRepository<ReservaInventario, Long> {
    List<ReservaInventario> findByIdPedido(Long idPedido);
}
