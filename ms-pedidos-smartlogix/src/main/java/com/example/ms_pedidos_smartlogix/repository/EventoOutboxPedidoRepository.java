package com.example.ms_pedidos_smartlogix.repository;

import com.example.ms_pedidos_smartlogix.model.EventoOutboxPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventoOutboxPedidoRepository extends JpaRepository<EventoOutboxPedido, Long> {
    List<EventoOutboxPedido> findByEstadoPublicacion(String estadoPublicacion);
}
