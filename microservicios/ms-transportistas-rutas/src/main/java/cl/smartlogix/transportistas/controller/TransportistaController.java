package cl.smartlogix.transportistas.controller;

import cl.smartlogix.transportistas.model.DisponibilidadTransportista;
import cl.smartlogix.transportistas.model.Ruta;
import cl.smartlogix.transportistas.model.TarifaRuta;
import cl.smartlogix.transportistas.model.Transportista;
import cl.smartlogix.transportistas.service.TransportistaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransportistaController {

    private final TransportistaService transportistaService;

    public TransportistaController(TransportistaService transportistaService) {
        this.transportistaService = transportistaService;
    }

    @GetMapping("/transportistas")
    public List<Transportista> listarTransportistas() {
        return transportistaService.listarTransportistas();
    }

    @GetMapping("/transportistas/activos")
    public List<Transportista> listarTransportistasActivos() {
        return transportistaService.listarTransportistasActivos();
    }

    @GetMapping("/transportistas/{idTransportista}")
    public ResponseEntity<Transportista> buscarTransportistaPorId(@PathVariable Long idTransportista) {
        return transportistaService.buscarTransportistaPorId(idTransportista)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/transportistas")
    public Transportista crearTransportista(@RequestBody Transportista transportista) {
        return transportistaService.crearTransportista(transportista);
    }

    @GetMapping("/rutas")
    public List<Ruta> listarRutas() {
        return transportistaService.listarRutas();
    }

    @GetMapping("/rutas/activas")
    public List<Ruta> listarRutasActivas() {
        return transportistaService.listarRutasActivas();
    }

    @GetMapping("/rutas/zona/{zonaCobertura}")
    public List<Ruta> buscarRutasPorZona(@PathVariable String zonaCobertura) {
        return transportistaService.buscarRutasPorZona(zonaCobertura);
    }

    @PostMapping("/rutas")
    public Ruta crearRuta(@RequestBody Ruta ruta) {
        return transportistaService.crearRuta(ruta);
    }

    @GetMapping("/tarifas")
    public List<TarifaRuta> listarTarifas() {
        return transportistaService.listarTarifas();
    }

    @GetMapping("/tarifas/transportista/{idTransportista}")
    public List<TarifaRuta> buscarTarifasPorTransportista(@PathVariable Long idTransportista) {
        return transportistaService.buscarTarifasPorTransportista(idTransportista);
    }

    @GetMapping("/tarifas/ruta/{idRuta}")
    public List<TarifaRuta> buscarTarifasPorRuta(@PathVariable Long idRuta) {
        return transportistaService.buscarTarifasPorRuta(idRuta);
    }

    @PostMapping("/tarifas")
    public TarifaRuta crearTarifa(@RequestBody TarifaRuta tarifaRuta) {
        return transportistaService.crearTarifa(tarifaRuta);
    }

    @GetMapping("/disponibilidades")
    public List<DisponibilidadTransportista> listarDisponibilidades() {
        return transportistaService.listarDisponibilidades();
    }

    @GetMapping("/disponibilidades/transportista/{idTransportista}")
    public List<DisponibilidadTransportista> buscarDisponibilidadPorTransportista(@PathVariable Long idTransportista) {
        return transportistaService.buscarDisponibilidadPorTransportista(idTransportista);
    }

    @GetMapping("/disponibilidades/fecha/{fecha}")
    public List<DisponibilidadTransportista> buscarDisponibilidadPorFecha(@PathVariable String fecha) {
        LocalDate fechaConvertida = LocalDate.parse(fecha);
        return transportistaService.buscarDisponibilidadPorFecha(fechaConvertida);
    }

    @GetMapping("/disponibilidades/zona/{zonaDisponible}")
    public List<DisponibilidadTransportista> buscarDisponibilidadPorZona(@PathVariable String zonaDisponible) {
        return transportistaService.buscarDisponibilidadPorZona(zonaDisponible);
    }

    @PostMapping("/disponibilidades")
    public DisponibilidadTransportista crearDisponibilidad(@RequestBody DisponibilidadTransportista disponibilidad) {
        return transportistaService.crearDisponibilidad(disponibilidad);
    }
}