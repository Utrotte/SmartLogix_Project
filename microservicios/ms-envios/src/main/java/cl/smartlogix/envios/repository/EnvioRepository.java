package cl.smartlogix.envios.repository;

import cl.smartlogix.envios.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EnvioRepository extends JpaRepository<Envio, Long> {

    List<Envio> findByIdPedido(Long idPedido);

    List<Envio> findByIdTransportista(Long idTransportista);

    Optional<Envio> findByCodigoEnvio(String codigoEnvio);
}