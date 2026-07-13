package com.smartlogix.usuarios.repository;

import com.smartlogix.usuarios.model.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/** Acceso a datos de la tabla usuario_sistema. */
public interface RepositorioUsuarioSistema extends JpaRepository<UsuarioSistema, Long> {
    Optional<UsuarioSistema> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
