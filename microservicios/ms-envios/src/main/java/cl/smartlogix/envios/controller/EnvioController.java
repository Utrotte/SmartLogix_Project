package cl.smartlogix.envios.controller;

import cl.smartlogix.envios.model.DireccionEnvio;
import cl.smartlogix.envios.model.Envio;
import cl.smartlogix.envios.model.EstadoEnvio;
import cl.smartlogix.envios.model.GuiaDespacho;
import cl.smartlogix.envios.model.SeguimientoEnvio;
import cl.smartlogix.envios.service.EnvioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @GetMapping
    public List<Envio> listarEnvios() {
        return envioService.listarEnvios();
    }

    @GetMapping("/{idEnvio}")
    public ResponseEntity<Envio> buscarEnvioPorId(@PathVariable Long idEnvio) {
        return envioService.buscarEnvioPorId(idEnvio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pedido/{idPedido}")
    public List<Envio> buscarEnviosPorPedido(@PathVariable Long idPedido) {
        return envioService.buscarEnviosPorPedido(idPedido);
    }

    @GetMapping("/transportista/{idTransportista}")
    public List<Envio> buscarEnviosPorTransportista(@PathVariable Long idTransportista) {
        return envioService.buscarEnviosPorTransportista(idTransportista);
    }

    @GetMapping("/codigo/{codigoEnvio}")
    public ResponseEntity<Envio> buscarEnvioPorCodigo(@PathVariable String codigoEnvio) {
        return envioService.buscarEnvioPorCodigo(codigoEnvio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Envio crearEnvio(@RequestBody Envio envio) {
        return envioService.crearEnvio(envio);
    }

    @PutMapping("/{idEnvio}/estado/{idEstadoEnvio}")
    public ResponseEntity<Envio> cambiarEstadoEnvio(
            @PathVariable Long idEnvio,
            @PathVariable Long idEstadoEnvio) {

        return envioService.cambiarEstadoEnvio(idEnvio, idEstadoEnvio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{idEnvio}/seguimientos")
    public List<SeguimientoEnvio> listarSeguimientosPorEnvio(@PathVariable Long idEnvio) {
        return envioService.listarSeguimientosPorEnvio(idEnvio);
    }

    @PostMapping("/{idEnvio}/seguimientos")
    public ResponseEntity<SeguimientoEnvio> registrarSeguimiento(
            @PathVariable Long idEnvio,
            @RequestBody Map<String, String> datos) {

        String estadoEvento = datos.get("estadoEvento");
        String descripcion = datos.get("descripcion");

        return envioService.registrarSeguimiento(idEnvio, estadoEvento, descripcion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{idEnvio}/guia")
    public ResponseEntity<GuiaDespacho> buscarGuiaPorEnvio(@PathVariable Long idEnvio) {
        return envioService.buscarGuiaPorEnvio(idEnvio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{idEnvio}/guia")
    public ResponseEntity<GuiaDespacho> crearGuiaDespacho(
            @PathVariable Long idEnvio,
            @RequestBody GuiaDespacho guiaDespacho) {

        return envioService.crearGuiaDespacho(idEnvio, guiaDespacho)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{idEnvio}/direccion")
    public ResponseEntity<DireccionEnvio> buscarDireccionPorEnvio(@PathVariable Long idEnvio) {
        return envioService.buscarDireccionPorEnvio(idEnvio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{idEnvio}/direccion")
    public ResponseEntity<DireccionEnvio> crearDireccionEnvio(
            @PathVariable Long idEnvio,
            @RequestBody DireccionEnvio direccionEnvio) {

        return envioService.crearDireccionEnvio(idEnvio, direccionEnvio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estados")
    public List<EstadoEnvio> listarEstadosEnvio() {
        return envioService.listarEstadosEnvio();
    }

    @PostMapping("/estados")
    public EstadoEnvio crearEstadoEnvio(@RequestBody EstadoEnvio estadoEnvio) {
        return envioService.crearEstadoEnvio(estadoEnvio);
    }
}
