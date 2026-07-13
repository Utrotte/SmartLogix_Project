package cl.smartlogix.transportistas.repository;

import cl.smartlogix.transportistas.model.ServicioTransporte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServicioTransporteRepository extends JpaRepository<ServicioTransporte, Long> {

    List<ServicioTransporte> findByTransportista_IdTransportista(Long idTransportista);
}