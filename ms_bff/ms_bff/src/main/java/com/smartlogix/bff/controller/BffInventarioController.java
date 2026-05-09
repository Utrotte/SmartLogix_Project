package com.smartlogix.bff.controller;

import com.smartlogix.bff.service.BffInventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bff/inventario")
public class BffInventarioController {

    private final BffInventarioService inventarioService;

    public BffInventarioController(BffInventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    // Categorías
    @PostMapping("/categorias")
    public ResponseEntity<Object> crearCategoria(@RequestBody Map<String, Object> request) {
        Object response = inventarioService.crearCategoria(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<?>> listarCategorias() {
        List<?> response = inventarioService.listarCategorias();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<Object> buscarCategoria(@PathVariable("id") Long id) {
        Object response = inventarioService.buscarCategoria(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<Object> actualizarCategoria(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> request) {
        Object response = inventarioService.actualizarCategoria(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/categorias/{id}/desactivar")
    public ResponseEntity<Void> desactivarCategoria(@PathVariable("id") Long id) {
        inventarioService.desactivarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    // Productos
    @PostMapping("/productos")
    public ResponseEntity<Object> crearProducto(@RequestBody Map<String, Object> request) {
        Object response = inventarioService.crearProducto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/productos")
    public ResponseEntity<List<?>> listarProductos() {
        List<?> response = inventarioService.listarProductos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<Object> buscarProducto(@PathVariable("id") Long id) {
        Object response = inventarioService.buscarProducto(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/productos/sku/{codigoSku}")
    public ResponseEntity<Object> buscarProductoPorSku(@PathVariable("codigoSku") String codigoSku) {
        Object response = inventarioService.buscarProductoPorSku(codigoSku);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<Object> actualizarProducto(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> request) {
        Object response = inventarioService.actualizarProducto(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/productos/{id}/desactivar")
    public ResponseEntity<Void> desactivarProducto(@PathVariable("id") Long id) {
        inventarioService.desactivarProducto(id);
        return ResponseEntity.noContent().build();
    }

    // Bodegas
    @PostMapping("/bodegas")
    public ResponseEntity<Object> crearBodega(@RequestBody Map<String, Object> request) {
        Object response = inventarioService.crearBodega(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/bodegas")
    public ResponseEntity<List<?>> listarBodegas() {
        List<?> response = inventarioService.listarBodegas();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bodegas/{id}")
    public ResponseEntity<Object> buscarBodega(@PathVariable("id") Long id) {
        Object response = inventarioService.buscarBodega(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/bodegas/{id}")
    public ResponseEntity<Object> actualizarBodega(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> request) {
        Object response = inventarioService.actualizarBodega(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/bodegas/{id}/desactivar")
    public ResponseEntity<Void> desactivarBodega(@PathVariable("id") Long id) {
        inventarioService.desactivarBodega(id);
        return ResponseEntity.noContent().build();
    }

    // Existencias
    @PostMapping("/existencias")
    public ResponseEntity<Object> crearExistencia(@RequestBody Map<String, Object> request) {
        Object response = inventarioService.crearExistencia(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/existencias/producto/{idProducto}")
    public ResponseEntity<List<?>> consultarExistenciasPorProducto(@PathVariable("idProducto") Long idProducto) {
        List<?> response = inventarioService.consultarExistenciasPorProducto(idProducto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/existencias/bodega/{idBodega}")
    public ResponseEntity<List<?>> consultarExistenciasPorBodega(@PathVariable("idBodega") Long idBodega) {
        List<?> response = inventarioService.consultarExistenciasPorBodega(idBodega);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/existencias/producto/{idProducto}/bodega/{idBodega}")
    public ResponseEntity<Object> consultarExistenciaPorProductoYBodega(
            @PathVariable("idProducto") Long idProducto,
            @PathVariable("idBodega") Long idBodega) {
        Object response = inventarioService.consultarExistenciaPorProductoYBodega(idProducto, idBodega);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/existencias/{idExistencia}/ajustar-stock")
    public ResponseEntity<Object> ajustarStock(
            @PathVariable("idExistencia") Long idExistencia,
            @RequestBody Map<String, Object> request) {
        Object response = inventarioService.ajustarStock(idExistencia, request);
        return ResponseEntity.ok(response);
    }

    // Reservas
    @PostMapping("/reservas")
    public ResponseEntity<Object> crearReserva(@RequestBody Map<String, Object> request) {
        Object response = inventarioService.crearReserva(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/reservas")
    public ResponseEntity<List<?>> listarReservas() {
        List<?> response = inventarioService.listarReservas();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservas/{idReserva}")
    public ResponseEntity<Object> buscarReserva(@PathVariable("idReserva") Long idReserva) {
        Object response = inventarioService.buscarReserva(idReserva);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservas/pedido/{idPedidoRef}")
    public ResponseEntity<List<?>> buscarReservasPorPedido(@PathVariable("idPedidoRef") Long idPedidoRef) {
        List<?> response = inventarioService.buscarReservasPorPedido(idPedidoRef);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reservas/{idReserva}/confirmar")
    public ResponseEntity<Object> confirmarReserva(@PathVariable("idReserva") Long idReserva) {
        Object response = inventarioService.confirmarReserva(idReserva);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reservas/{idReserva}/liberar")
    public ResponseEntity<Object> liberarReserva(@PathVariable("idReserva") Long idReserva) {
        Object response = inventarioService.liberarReserva(idReserva);
        return ResponseEntity.ok(response);
    }

    // Movimientos
    @GetMapping("/movimientos")
    public ResponseEntity<List<?>> listarMovimientos() {
        List<?> response = inventarioService.listarMovimientos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/movimientos/{idMovimiento}")
    public ResponseEntity<Object> buscarMovimiento(@PathVariable("idMovimiento") Long idMovimiento) {
        Object response = inventarioService.buscarMovimiento(idMovimiento);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/movimientos/producto/{idProducto}")
    public ResponseEntity<List<?>> listarMovimientosPorProducto(@PathVariable("idProducto") Long idProducto) {
        List<?> response = inventarioService.listarMovimientosPorProducto(idProducto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/movimientos/bodega/{idBodega}")
    public ResponseEntity<List<?>> listarMovimientosPorBodega(@PathVariable("idBodega") Long idBodega) {
        List<?> response = inventarioService.listarMovimientosPorBodega(idBodega);
        return ResponseEntity.ok(response);
    }
}
