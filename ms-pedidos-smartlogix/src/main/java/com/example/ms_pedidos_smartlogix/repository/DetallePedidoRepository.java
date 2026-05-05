package com.example.ms_pedidos_smartlogix.repository;

import com.example.ms_pedidos_smartlogix.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByPedido_IdPedido(Long idPedido);
}
