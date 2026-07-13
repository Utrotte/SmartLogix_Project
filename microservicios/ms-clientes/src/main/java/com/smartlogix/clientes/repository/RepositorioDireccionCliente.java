package com.smartlogix.clientes.repository;

import com.smartlogix.clientes.model.DireccionCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio JPA para la entidad DireccionCliente.
 */
public interface RepositorioDireccionCliente extends JpaRepository<DireccionCliente, Long> {

    List<DireccionCliente> findByClienteIdCliente(Long idCliente);
}
