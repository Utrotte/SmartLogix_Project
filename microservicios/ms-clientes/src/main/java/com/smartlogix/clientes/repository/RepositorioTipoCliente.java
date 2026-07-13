package com.smartlogix.clientes.repository;

import com.smartlogix.clientes.model.TipoCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad TipoCliente.
 */
public interface RepositorioTipoCliente extends JpaRepository<TipoCliente, Long> {

    Optional<TipoCliente> findByNombre(String nombre);
}
