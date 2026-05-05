package com.smartlogix.inventario_service.repository;

import com.smartlogix.inventario_service.model.ReservaInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaInventarioRepository extends JpaRepository<ReservaInventario, Long> {

    List<ReservaInventario> findByIdPedidoRef(Long idPedidoRef);
}
