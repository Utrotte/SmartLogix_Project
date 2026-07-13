package cl.smartlogix.transportistas.service;

import cl.smartlogix.transportistas.model.DisponibilidadTransportista;
import cl.smartlogix.transportistas.model.Ruta;
import cl.smartlogix.transportistas.model.TarifaRuta;
import cl.smartlogix.transportistas.model.Transportista;
import cl.smartlogix.transportistas.repository.DisponibilidadTransportistaRepository;
import cl.smartlogix.transportistas.repository.RutaRepository;
import cl.smartlogix.transportistas.repository.TarifaRutaRepository;
import cl.smartlogix.transportistas.repository.TransportistaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransportistaService {

    private final TransportistaRepository transportistaRepository;
    private final RutaRepository rutaRepository;
    private final TarifaRutaRepository tarifaRutaRepository;
    private final DisponibilidadTransportistaRepository disponibilidadRepository;

    public TransportistaService(
            TransportistaRepository transportistaRepository,
            RutaRepository rutaRepository,
            TarifaRutaRepository tarifaRutaRepository,
            DisponibilidadTransportistaRepository disponibilidadRepository) {
        this.transportistaRepository = transportistaRepository;
        this.rutaRepository = rutaRepository;
        this.tarifaRutaRepository = tarifaRutaRepository;
        this.disponibilidadRepository = disponibilidadRepository;
    }

    public List<Transportista> listarTransportistas() {
        return transportistaRepository.findAll();
    }

    public List<Transportista> listarTransportistasActivos() {
        return transportistaRepository.findByActivoTrue();
    }

    public Optional<Transportista> buscarTransportistaPorId(Long idTransportista) {
        return transportistaRepository.findById(idTransportista);
    }

    public Transportista crearTransportista(Transportista transportista) {
        return transportistaRepository.save(transportista);
    }

    public List<Ruta> listarRutas() {
        return rutaRepository.findAll();
    }

    public List<Ruta> listarRutasActivas() {
        return rutaRepository.findByActivaTrue();
    }

    public List<Ruta> buscarRutasPorZona(String zonaCobertura) {
        return rutaRepository.findByZonaCobertura(zonaCobertura);
    }

    public Ruta crearRuta(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    public List<TarifaRuta> listarTarifas() {
        return tarifaRutaRepository.findAll();
    }

    public List<TarifaRuta> buscarTarifasPorTransportista(Long idTransportista) {
        return tarifaRutaRepository.findByTransportista_IdTransportista(idTransportista);
    }

    public List<TarifaRuta> buscarTarifasPorRuta(Long idRuta) {
        return tarifaRutaRepository.findByRuta_IdRuta(idRuta);
    }

    public TarifaRuta crearTarifa(TarifaRuta tarifaRuta) {
        return tarifaRutaRepository.save(tarifaRuta);
    }

    public List<DisponibilidadTransportista> listarDisponibilidades() {
        return disponibilidadRepository.findAll();
    }

    public List<DisponibilidadTransportista> buscarDisponibilidadPorTransportista(Long idTransportista) {
        return disponibilidadRepository.findByTransportista_IdTransportista(idTransportista);
    }

    public List<DisponibilidadTransportista> buscarDisponibilidadPorFecha(LocalDate fecha) {
        return disponibilidadRepository.findByFechaAndDisponibleTrue(fecha);
    }

    public List<DisponibilidadTransportista> buscarDisponibilidadPorZona(String zonaDisponible) {
        return disponibilidadRepository.findByZonaDisponibleAndDisponibleTrue(zonaDisponible);
    }

    public DisponibilidadTransportista crearDisponibilidad(DisponibilidadTransportista disponibilidad) {
        return disponibilidadRepository.save(disponibilidad);
    }
}