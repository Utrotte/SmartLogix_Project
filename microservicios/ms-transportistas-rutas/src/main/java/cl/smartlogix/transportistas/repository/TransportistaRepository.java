package cl.smartlogix.transportistas.repository;

import cl.smartlogix.transportistas.model.Transportista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransportistaRepository extends JpaRepository<Transportista, Long> {

    List<Transportista> findByActivoTrue();
}