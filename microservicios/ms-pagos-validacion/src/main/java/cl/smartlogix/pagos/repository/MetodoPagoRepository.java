package cl.smartlogix.pagos.repository;

import cl.smartlogix.pagos.model.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {
}