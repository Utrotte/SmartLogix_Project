package cl.programadormaldito.ms_envios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.programadormaldito.ms_envios.model.PaqueteEnvio;

public interface PaqueteEnvioRepository extends JpaRepository<PaqueteEnvio, String> {
}
