package cl.smartlogix.pagos.repository;

import cl.smartlogix.pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByIdPedido(Long idPedido);

    List<Pago> findByIdCliente(Long idCliente);
}