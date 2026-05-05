package com.example.ms_pedidos_smartlogix.repository;

import com.example.ms_pedidos_smartlogix.model.EstadoPedidoHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EstadoPedidoHistorialRepository extends JpaRepository<EstadoPedidoHistorial, Long> {
    List<EstadoPedidoHistorial> findByPedido_IdPedido(Long idPedido);
}
