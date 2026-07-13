package cl.smartlogix.envios.repository;

import cl.smartlogix.envios.model.SeguimientoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeguimientoEnvioRepository extends JpaRepository<SeguimientoEnvio, Long> {

    List<SeguimientoEnvio> findByEnvio_IdEnvio(Long idEnvio);
}