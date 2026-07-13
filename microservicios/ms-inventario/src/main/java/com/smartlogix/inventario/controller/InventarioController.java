package com.smartlogix.inventario.controller;

import com.smartlogix.inventario.dto.*;
import com.smartlogix.inventario.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/existencias")
    public List<ExistenciaResponse> listarExistencias() {
        return inventarioService.listarExistencias();
    }

    @GetMapping("/producto/{idProducto}")
    public List<ExistenciaResponse> listarPorProducto(@PathVariable Long idProducto) {
        return inventarioService.listarPorProducto(idProducto);
    }

    @GetMapping("/bodega/{idBodega}")
    public List<ExistenciaResponse> listarPorBodega(@PathVariable Long idBodega) {
        return inventarioService.listarPorBodega(idBodega);
    }

    @PostMapping("/existencias")
    @ResponseStatus(HttpStatus.CREATED)
    public ExistenciaResponse crearExistencia(@Valid @RequestBody CrearExistenciaRequest req) {
        return inventarioService.crearExistencia(req);
    }

    @PostMapping("/movimientos")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoInventarioResponse crearMovimiento(@Valid @RequestBody CrearMovimientoRequest req) {
        return inventarioService.crearMovimiento(req);
    }

    @GetMapping("/movimientos/producto/{idProducto}")
    public List<MovimientoInventarioResponse> movimientosPorProducto(@PathVariable Long idProducto) {
        return inventarioService.movimientosPorProducto(idProducto);
    }

    @PostMapping("/reservas")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaInventarioResponse crearReserva(@Valid @RequestBody CrearReservaRequest req) {
        return inventarioService.crearReserva(req);
    }

    @PostMapping("/reservas/{idReserva}/liberar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void liberarReserva(@PathVariable Long idReserva) {
        inventarioService.liberarReserva(idReserva);
    }

    @GetMapping("/reservas/pedido/{idPedido}")
    public List<ReservaInventarioResponse> reservasPorPedido(@PathVariable Long idPedido) {
        return inventarioService.reservasPorPedido(idPedido);
    }
}
