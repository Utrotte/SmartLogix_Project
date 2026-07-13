package cl.smartlogix.envios.repository;

import cl.smartlogix.envios.model.DireccionEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DireccionEnvioRepository extends JpaRepository<DireccionEnvio, Long> {

    Optional<DireccionEnvio> findByEnvio_IdEnvio(Long idEnvio);
}