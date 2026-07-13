package cl.smartlogix.pagos.repository;

import cl.smartlogix.pagos.model.ComprobantePago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {

    Optional<ComprobantePago> findByPago_IdPago(Long idPago);
}