package cl.smartlogix.envios.repository;

import cl.smartlogix.envios.model.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoEnvioRepository extends JpaRepository<EstadoEnvio, Long> {
}