package cl.smartlogix.transportistas.repository;

import cl.smartlogix.transportistas.model.TarifaRuta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TarifaRutaRepository extends JpaRepository<TarifaRuta, Long> {

    List<TarifaRuta> findByTransportista_IdTransportista(Long idTransportista);

    List<TarifaRuta> findByRuta_IdRuta(Long idRuta);
}