package com.smartlogix.usuarios.repository;

import com.smartlogix.usuarios.model.SesionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RepositorioSesionUsuario extends JpaRepository<SesionUsuario, Long> {
    Optional<SesionUsuario> findByTokenJwtAndActivaTrue(String tokenJwt);
}
