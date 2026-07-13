package cl.smartlogix.transportistas.repository;

import cl.smartlogix.transportistas.model.DisponibilidadTransportista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface DisponibilidadTransportistaRepository extends JpaRepository<DisponibilidadTransportista, Long> {

    List<DisponibilidadTransportista> findByTransportista_IdTransportista(Long idTransportista);

    List<DisponibilidadTransportista> findByFechaAndDisponibleTrue(LocalDate fecha);

    List<DisponibilidadTransportista> findByZonaDisponibleAndDisponibleTrue(String zonaDisponible);
}