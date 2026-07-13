package com.smartlogix.clientes.repository;

import com.smartlogix.clientes.model.ContactoCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio JPA para la entidad ContactoCliente.
 */
public interface RepositorioContactoCliente extends JpaRepository<ContactoCliente, Long> {

    List<ContactoCliente> findByClienteIdCliente(Long idCliente);
}
