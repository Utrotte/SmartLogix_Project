package cl.programadormaldito.ms_envios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.programadormaldito.ms_envios.model.Envio;

public interface EnvioRepository extends JpaRepository<Envio, String> {

    // permite buscar el envío a partir del id del pedido que lo originó
    Optional<Envio> findByIdPedidoRef(String idPedidoRef);

    // útil para filtrar envíos por estado (PENDIENTE, EN_TRANSITO, etc.)
    List<Envio> findByEstado(String estado);

    Optional<Envio> findByCodigoEnvio(String codigoEnvio);
}
