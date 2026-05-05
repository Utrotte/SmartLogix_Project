package com.example.ms_pedidos_smartlogix.repository;

import com.example.ms_pedidos_smartlogix.model.DireccionEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DireccionEntregaRepository extends JpaRepository<DireccionEntrega, Long> {
    Optional<DireccionEntrega> findByPedido_IdPedido(Long idPedido);
    boolean existsByPedido_IdPedido(Long idPedido);
}
