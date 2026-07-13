package cl.smartlogix.transportistas.repository;

import cl.smartlogix.transportistas.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RutaRepository extends JpaRepository<Ruta, Long> {

    List<Ruta> findByActivaTrue();

    List<Ruta> findByZonaCobertura(String zonaCobertura);
}