package cl.smartlogix.envios.service;

import cl.smartlogix.envios.model.DireccionEnvio;
import cl.smartlogix.envios.model.Envio;
import cl.smartlogix.envios.model.EstadoEnvio;
import cl.smartlogix.envios.model.GuiaDespacho;
import cl.smartlogix.envios.model.SeguimientoEnvio;
import cl.smartlogix.envios.repository.DireccionEnvioRepository;
import cl.smartlogix.envios.repository.EnvioRepository;
import cl.smartlogix.envios.repository.EstadoEnvioRepository;
import cl.smartlogix.envios.repository.GuiaDespachoRepository;
import cl.smartlogix.envios.repository.SeguimientoEnvioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final EstadoEnvioRepository estadoEnvioRepository;
    private final SeguimientoEnvioRepository seguimientoEnvioRepository;
    private final GuiaDespachoRepository guiaDespachoRepository;
    private final DireccionEnvioRepository direccionEnvioRepository;

    public EnvioService(
            EnvioRepository envioRepository,
            EstadoEnvioRepository estadoEnvioRepository,
            SeguimientoEnvioRepository seguimientoEnvioRepository,
            GuiaDespachoRepository guiaDespachoRepository,
            DireccionEnvioRepository direccionEnvioRepository) {
        this.envioRepository = envioRepository;
        this.estadoEnvioRepository = estadoEnvioRepository;
        this.seguimientoEnvioRepository = seguimientoEnvioRepository;
        this.guiaDespachoRepository = guiaDespachoRepository;
        this.direccionEnvioRepository = direccionEnvioRepository;
    }

    public List<Envio> listarEnvios() {
        return envioRepository.findAll();
    }

    public Optional<Envio> buscarEnvioPorId(Long idEnvio) {
        return envioRepository.findById(idEnvio);
    }

    public List<Envio> buscarEnviosPorPedido(Long idPedido) {
        return envioRepository.findByIdPedido(idPedido);
    }

    public List<Envio> buscarEnviosPorTransportista(Long idTransportista) {
        return envioRepository.findByIdTransportista(idTransportista);
    }

    public Optional<Envio> buscarEnvioPorCodigo(String codigoEnvio) {
        return envioRepository.findByCodigoEnvio(codigoEnvio);
    }

    public Envio crearEnvio(Envio envio) {
        if (envio.getEstadoEnvio() != null && envio.getEstadoEnvio().getIdEstadoEnvio() != null) {
            EstadoEnvio estado = estadoEnvioRepository.findById(envio.getEstadoEnvio().getIdEstadoEnvio())
                    .orElseThrow(() -> new IllegalArgumentException("Estado de envío no válido"));
            envio.setEstadoEnvio(estado);
        }
        return envioRepository.save(envio);
    }

    public Optional<Envio> cambiarEstadoEnvio(Long idEnvio, Long idEstadoEnvio) {
        Optional<Envio> envioEncontrado = envioRepository.findById(idEnvio);
        Optional<EstadoEnvio> estadoEncontrado = estadoEnvioRepository.findById(idEstadoEnvio);

        if (envioEncontrado.isEmpty() || estadoEncontrado.isEmpty()) {
            return Optional.empty();
        }

        Envio envio = envioEncontrado.get();
        envio.setEstadoEnvio(estadoEncontrado.get());

        return Optional.of(envioRepository.save(envio));
    }

    public List<SeguimientoEnvio> listarSeguimientosPorEnvio(Long idEnvio) {
        return seguimientoEnvioRepository.findByEnvio_IdEnvio(idEnvio);
    }

    public Optional<SeguimientoEnvio> registrarSeguimiento(Long idEnvio, String estadoEvento, String descripcion) {
        Optional<Envio> envioEncontrado = envioRepository.findById(idEnvio);

        if (envioEncontrado.isEmpty()) {
            return Optional.empty();
        }

        SeguimientoEnvio seguimiento = new SeguimientoEnvio();
        seguimiento.setEnvio(envioEncontrado.get());
        seguimiento.setEstadoEvento(estadoEvento);
        seguimiento.setDescripcion(descripcion);

        return Optional.of(seguimientoEnvioRepository.save(seguimiento));
    }

    public Optional<GuiaDespacho> buscarGuiaPorEnvio(Long idEnvio) {
        return guiaDespachoRepository.findByEnvio_IdEnvio(idEnvio);
    }

    public Optional<GuiaDespacho> crearGuiaDespacho(Long idEnvio, GuiaDespacho guiaDespacho) {
        Optional<Envio> envioEncontrado = envioRepository.findById(idEnvio);

        if (envioEncontrado.isEmpty()) {
            return Optional.empty();
        }

        guiaDespacho.setEnvio(envioEncontrado.get());

        return Optional.of(guiaDespachoRepository.save(guiaDespacho));
    }

    public Optional<DireccionEnvio> buscarDireccionPorEnvio(Long idEnvio) {
        return direccionEnvioRepository.findByEnvio_IdEnvio(idEnvio);
    }

    public Optional<DireccionEnvio> crearDireccionEnvio(Long idEnvio, DireccionEnvio direccionEnvio) {
        Optional<Envio> envioEncontrado = envioRepository.findById(idEnvio);

        if (envioEncontrado.isEmpty()) {
            return Optional.empty();
        }

        direccionEnvio.setEnvio(envioEncontrado.get());

        return Optional.of(direccionEnvioRepository.save(direccionEnvio));
    }

    public List<EstadoEnvio> listarEstadosEnvio() {
        return estadoEnvioRepository.findAll();
    }

    public EstadoEnvio crearEstadoEnvio(EstadoEnvio estadoEnvio) {
        return estadoEnvioRepository.save(estadoEnvio);
    }
}
