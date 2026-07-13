package cl.smartlogix.envios.repository;

import cl.smartlogix.envios.model.GuiaDespacho;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GuiaDespachoRepository extends JpaRepository<GuiaDespacho, Long> {

    Optional<GuiaDespacho> findByEnvio_IdEnvio(Long idEnvio);

    Optional<GuiaDespacho> findByNumeroGuia(String numeroGuia);
}