package cl.smartlogix.pagos.repository;

import cl.smartlogix.pagos.model.ValidacionPago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ValidacionPagoRepository extends JpaRepository<ValidacionPago, Long> {

    List<ValidacionPago> findByPago_IdPago(Long idPago);
}