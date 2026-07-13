package cl.smartlogix.pagos.service;

import cl.smartlogix.pagos.model.EstadoPago;
import cl.smartlogix.pagos.model.Pago;
import cl.smartlogix.pagos.model.ValidacionPago;
import cl.smartlogix.pagos.repository.EstadoPagoRepository;
import cl.smartlogix.pagos.repository.PagoRepository;
import cl.smartlogix.pagos.repository.ValidacionPagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final EstadoPagoRepository estadoPagoRepository;
    private final ValidacionPagoRepository validacionPagoRepository;

    public PagoService(
            PagoRepository pagoRepository,
            EstadoPagoRepository estadoPagoRepository,
            ValidacionPagoRepository validacionPagoRepository) {
        this.pagoRepository = pagoRepository;
        this.estadoPagoRepository = estadoPagoRepository;
        this.validacionPagoRepository = validacionPagoRepository;
    }

    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> buscarPorId(Long idPago) {
        return pagoRepository.findById(idPago);
    }

    public List<Pago> buscarPorPedido(Long idPedido) {
        return pagoRepository.findByIdPedido(idPedido);
    }

    public Pago crearPago(Pago pago) {
        return pagoRepository.save(pago);
    }

    public Optional<Pago> cambiarEstado(Long idPago, Long idEstadoPago) {
        Optional<Pago> pagoEncontrado = pagoRepository.findById(idPago);
        Optional<EstadoPago> estadoEncontrado = estadoPagoRepository.findById(idEstadoPago);

        if (pagoEncontrado.isEmpty() || estadoEncontrado.isEmpty()) {
            return Optional.empty();
        }

        Pago pago = pagoEncontrado.get();
        pago.setEstadoPago(estadoEncontrado.get());

        return Optional.of(pagoRepository.save(pago));
    }

    public Optional<ValidacionPago> registrarValidacion(Long idPago, String resultado, String detalle) {
        Optional<Pago> pagoEncontrado = pagoRepository.findById(idPago);

        if (pagoEncontrado.isEmpty()) {
            return Optional.empty();
        }

        ValidacionPago validacion = new ValidacionPago();
        validacion.setPago(pagoEncontrado.get());
        validacion.setResultado(resultado);
        validacion.setDetalle(detalle);

        return Optional.of(validacionPagoRepository.save(validacion));
    }
}