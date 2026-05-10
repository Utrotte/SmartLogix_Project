package cl.programadormaldito.ms_envios.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cl.programadormaldito.ms_envios.dto.EnvioRequestDTO;
import cl.programadormaldito.ms_envios.dto.EnvioResponseDTO;
import cl.programadormaldito.ms_envios.model.DireccionEnvio;
import cl.programadormaldito.ms_envios.model.Envio;
import cl.programadormaldito.ms_envios.model.EventoOutboxEnvio;
import cl.programadormaldito.ms_envios.model.PaqueteEnvio;
import cl.programadormaldito.ms_envios.model.Transportista;
import cl.programadormaldito.ms_envios.repository.DireccionEnvioRepository;
import cl.programadormaldito.ms_envios.repository.EnvioRepository;
import cl.programadormaldito.ms_envios.repository.EventoOutboxEnvioRepository;
import cl.programadormaldito.ms_envios.repository.PaqueteEnvioRepository;
import cl.programadormaldito.ms_envios.repository.TransportistaRepository;
import cl.programadormaldito.ms_envios.integration.service.EnvioIntegrationService;

@Service
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final DireccionEnvioRepository direccionEnvioRepository;
    private final PaqueteEnvioRepository paqueteEnvioRepository;
    private final EventoOutboxEnvioRepository eventoOutboxEnvioRepository;
    private final TransportistaRepository transportistaRepository;
    private final EnvioIntegrationService envioIntegrationService;

    // Constructor Injection - más testeable y seguro
    public EnvioService(EnvioRepository envioRepository,
                        DireccionEnvioRepository direccionEnvioRepository,
                        PaqueteEnvioRepository paqueteEnvioRepository,
                        EventoOutboxEnvioRepository eventoOutboxEnvioRepository,
                        TransportistaRepository transportistaRepository,
                        EnvioIntegrationService envioIntegrationService) {
        this.envioRepository = envioRepository;
        this.direccionEnvioRepository = direccionEnvioRepository;
        this.paqueteEnvioRepository = paqueteEnvioRepository;
        this.eventoOutboxEnvioRepository = eventoOutboxEnvioRepository;
        this.transportistaRepository = transportistaRepository;
        this.envioIntegrationService = envioIntegrationService;
    }

    // ---- Factory Method: métodos privados para construir los objetos de dominio ----

    // construye la entidad Envio a partir de los datos del request
    private Envio construirEnvio(EnvioRequestDTO dto) {
        Envio envio = new Envio();
        envio.setIdPedidoRef(dto.getIdPedidoRef());
        envio.setFechaProgramada(dto.getFechaProgramada());
        envio.setFechaEstimada(dto.getFechaEstimada());
        envio.setCostoTotal(dto.getCostoTotal());
        return envio;
    }

    private DireccionEnvio construirDireccion(EnvioRequestDTO dto, Envio envio) {
        DireccionEnvio direccion = new DireccionEnvio();
        direccion.setCalle(dto.getCalle());
        direccion.setNumero(dto.getNumero());
        direccion.setComuna(dto.getComuna());
        direccion.setCiudad(dto.getCiudad());
        direccion.setRegion(dto.getRegion());
        direccion.setReferencia(dto.getReferencia());
        direccion.setEnvio(envio); // asocia la dirección al envío recién creado
        return direccion;
    }

    private PaqueteEnvio construirPaquete(EnvioRequestDTO dto, Envio envio) {
        PaqueteEnvio paquete = new PaqueteEnvio();
        paquete.setPesoKg(dto.getPesoKg());
        paquete.setAltoCm(dto.getAltoCm());
        paquete.setAnchoCm(dto.getAnchoCm());
        paquete.setLargoCm(dto.getLargoCm());
        paquete.setDescripcionContenido(dto.getDescripcionContenido());
        paquete.setEnvio(envio);
        return paquete;
    }

    // crea un evento para el patrón Outbox; el payload es un JSON mínimo con los datos del evento
    private EventoOutboxEnvio construirEventoOutbox(String idEnvio, String tipoEvento) {
        EventoOutboxEnvio evento = new EventoOutboxEnvio();
        evento.setIdEnvioRef(idEnvio);
        evento.setTipoEvento(tipoEvento);
        evento.setPayload("{\"idEnvio\":\"" + idEnvio + "\",\"evento\":\"" + tipoEvento + "\"}");
        return evento;
    }

    // mapea la entidad Envio al DTO de respuesta
    private EnvioResponseDTO construirRespuesta(Envio envio) {
        EnvioResponseDTO respuesta = new EnvioResponseDTO();
        respuesta.setId(envio.getId());
        respuesta.setIdPedidoRef(envio.getIdPedidoRef());
        respuesta.setCodigoEnvio(envio.getCodigoEnvio());
        respuesta.setEstado(envio.getEstado());
        respuesta.setFechaProgramada(envio.getFechaProgramada());
        respuesta.setFechaEstimada(envio.getFechaEstimada());
        respuesta.setCostoTotal(envio.getCostoTotal());

        // solo si ya tiene transportista asignado
        if (envio.getTransportista() != null) {
            respuesta.setIdTransportista(envio.getTransportista().getId());
            respuesta.setNombreTransportista(envio.getTransportista().getNombre());
        }

        return respuesta;
    }

    // ---- Lógica de negocio ----

    public EnvioResponseDTO envioCrear(EnvioRequestDTO dto) {
        // 0. Validar que el pedido existe y está CONFIRMADO
        if (dto.getIdPedidoRef() == null || dto.getIdPedidoRef().isEmpty()) {
            throw new IllegalArgumentException("idPedidoRef no puede estar vacío");
        }

        try {
            Long idPedido = Long.parseLong(dto.getIdPedidoRef());
            this.envioIntegrationService.validarPedidoParaEnvio(idPedido);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("idPedidoRef debe ser un número válido");
        }

        // 1. crea y guarda el envío principal
        Envio envio = construirEnvio(dto);
        this.envioRepository.save(envio);

        // 2. persiste la dirección y el paquete vinculados al envío
        DireccionEnvio direccion = construirDireccion(dto, envio);
        this.direccionEnvioRepository.save(direccion);

        PaqueteEnvio paquete = construirPaquete(dto, envio);
        this.paqueteEnvioRepository.save(paquete);

        // 3. registra el evento outbox para notificar a otros microservicios
        EventoOutboxEnvio evento = construirEventoOutbox(envio.getId(), "ENVIO_CREADO");
        this.eventoOutboxEnvioRepository.save(evento);

        return construirRespuesta(envio);
    }

    public List<EnvioResponseDTO> envioListar() {
        List<Envio> envios = this.envioRepository.findAll();
        List<EnvioResponseDTO> respuestas = new ArrayList<>();

        for (Envio envio : envios) {
            respuestas.add(construirRespuesta(envio));
        }

        return respuestas;
    }

    public EnvioResponseDTO envioBuscarPorId(String id) {
        Envio envio = this.envioRepository.findById(id).orElse(null);

        if (envio == null) {
            return null;
        }

        return construirRespuesta(envio);
    }

    public EnvioResponseDTO envioBuscarPorPedido(String idPedidoRef) {
        Envio envio = this.envioRepository.findByIdPedidoRef(idPedidoRef).orElse(null);

        if (envio == null) {
            return null;
        }

        return construirRespuesta(envio);
    }

    public EnvioResponseDTO envioActualizarEstado(String id, String nuevoEstado) {
        Envio envio = this.envioRepository.findById(id).orElse(null);

        if (envio == null) {
            return null;
        }

        envio.setEstado(nuevoEstado);
        this.envioRepository.save(envio);

        // deja registro del cambio de estado en el outbox
        EventoOutboxEnvio evento = construirEventoOutbox(envio.getId(), "ESTADO_ACTUALIZADO_" + nuevoEstado);
        this.eventoOutboxEnvioRepository.save(evento);

        return construirRespuesta(envio);
    }

    public EnvioResponseDTO envioAsignarTransportista(String idEnvio, String idTransportista) {
        Envio envio = this.envioRepository.findById(idEnvio).orElse(null);
        Transportista transportista = this.transportistaRepository.findById(idTransportista).orElse(null);

        // si alguno no existe, no se hace nada
        if (envio == null || transportista == null) {
            return null;
        }

        envio.setTransportista(transportista);
        envio.setEstado("ASIGNADO"); // el envío pasa a estado ASIGNADO al tener transportista
        this.envioRepository.save(envio);

        EventoOutboxEnvio evento = construirEventoOutbox(envio.getId(), "TRANSPORTISTA_ASIGNADO");
        this.eventoOutboxEnvioRepository.save(evento);

        return construirRespuesta(envio);
    }
}
