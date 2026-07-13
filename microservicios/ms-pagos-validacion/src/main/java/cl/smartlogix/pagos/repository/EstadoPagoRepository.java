package cl.smartlogix.pagos.repository;

import cl.smartlogix.pagos.model.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoPagoRepository extends JpaRepository<EstadoPago, Long> {
}