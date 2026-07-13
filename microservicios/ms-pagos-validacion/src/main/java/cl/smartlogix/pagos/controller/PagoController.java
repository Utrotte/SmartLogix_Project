package cl.smartlogix.pagos.controller;

import cl.smartlogix.pagos.model.Pago;
import cl.smartlogix.pagos.model.ValidacionPago;
import cl.smartlogix.pagos.service.PagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public List<Pago> listarPagos() {
        return pagoService.listarPagos();
    }

    @GetMapping("/{idPago}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable Long idPago) {
        return pagoService.buscarPorId(idPago)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pedido/{idPedido}")
    public List<Pago> buscarPorPedido(@PathVariable Long idPedido) {
        return pagoService.buscarPorPedido(idPedido);
    }

    @PostMapping
    public Pago crearPago(@RequestBody Pago pago) {
        return pagoService.crearPago(pago);
    }

    @PutMapping("/{idPago}/estado/{idEstadoPago}")
    public ResponseEntity<Pago> cambiarEstado(
            @PathVariable Long idPago,
            @PathVariable Long idEstadoPago) {

        return pagoService.cambiarEstado(idPago, idEstadoPago)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{idPago}/validaciones")
    public ResponseEntity<ValidacionPago> registrarValidacion(
            @PathVariable Long idPago,
            @RequestBody Map<String, String> datos) {

        String resultado = datos.get("resultado");
        String detalle = datos.get("detalle");

        return pagoService.registrarValidacion(idPago, resultado, detalle)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}