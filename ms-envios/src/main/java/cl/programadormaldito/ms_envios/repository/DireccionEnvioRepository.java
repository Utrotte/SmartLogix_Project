package cl.programadormaldito.ms_envios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.programadormaldito.ms_envios.model.DireccionEnvio;

public interface DireccionEnvioRepository extends JpaRepository<DireccionEnvio, String> {
}
