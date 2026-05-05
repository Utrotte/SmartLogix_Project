package com.example.ms_pedidos_smartlogix.repository;

import com.example.ms_pedidos_smartlogix.model.PagoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagoPedidoRepository extends JpaRepository<PagoPedido, Long> {
    List<PagoPedido> findByPedido_IdPedido(Long idPedido);
}
