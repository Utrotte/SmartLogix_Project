package com.smartlogix.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Cliente Feign para el servicio de Inventario
 * Nombres de servicio registrados en Eureka:
 * - ms-inventario (por defecto)
 * 
 * Si el nombre es distinto, cambiar en @FeignClient(name = "...")
 */
@FeignClient(name = "ms-inventario", path = "/api/inventario")
public interface InventarioClient {

    // Categorías
    @PostMapping("/categorias")
    Object crearCategoria(@RequestBody Map<String, Object> request);

    @GetMapping("/categorias")
    List<?> listarCategorias();

    @GetMapping("/categorias/{id}")
    Object buscarCategoria(@PathVariable("id") Long id);

    @PutMapping("/categorias/{id}")
    Object actualizarCategoria(@PathVariable("id") Long id, @RequestBody Map<String, Object> request);

    @PatchMapping("/categorias/{id}/desactivar")
    void desactivarCategoria(@PathVariable("id") Long id);

    // Productos
    @PostMapping("/productos")
    Object crearProducto(@RequestBody Map<String, Object> request);

    @GetMapping("/productos")
    List<?> listarProductos();

    @GetMapping("/productos/{id}")
    Object buscarProducto(@PathVariable("id") Long id);

    @GetMapping("/productos/sku/{codigoSku}")
    Object buscarProductoPorSku(@PathVariable("codigoSku") String codigoSku);

    @PutMapping("/productos/{id}")
    Object actualizarProducto(@PathVariable("id") Long id, @RequestBody Map<String, Object> request);

    @PatchMapping("/productos/{id}/desactivar")
    void desactivarProducto(@PathVariable("id") Long id);

    // Bodegas
    @PostMapping("/bodegas")
    Object crearBodega(@RequestBody Map<String, Object> request);

    @GetMapping("/bodegas")
    List<?> listarBodegas();

    @GetMapping("/bodegas/{id}")
    Object buscarBodega(@PathVariable("id") Long id);

    @PutMapping("/bodegas/{id}")
    Object actualizarBodega(@PathVariable("id") Long id, @RequestBody Map<String, Object> request);

    @PatchMapping("/bodegas/{id}/desactivar")
    void desactivarBodega(@PathVariable("id") Long id);

    // Existencias
    @PostMapping("/existencias")
    Object crearExistencia(@RequestBody Map<String, Object> request);

    @GetMapping("/existencias/producto/{idProducto}")
    List<?> consultarExistenciasPorProducto(@PathVariable("idProducto") Long idProducto);

    @GetMapping("/existencias/bodega/{idBodega}")
    List<?> consultarExistenciasPorBodega(@PathVariable("idBodega") Long idBodega);

    @GetMapping("/existencias/producto/{idProducto}/bodega/{idBodega}")
    Object consultarExistenciaPorProductoYBodega(@PathVariable("idProducto") Long idProducto,
                                                  @PathVariable("idBodega") Long idBodega);

    @PutMapping("/existencias/{idExistencia}/ajustar-stock")
    Object ajustarStock(@PathVariable("idExistencia") Long idExistencia,
                       @RequestBody Map<String, Object> request);

    // Reservas
    @PostMapping("/reservas")
    Object crearReserva(@RequestBody Map<String, Object> request);

    @GetMapping("/reservas")
    List<?> listarReservas();

    @GetMapping("/reservas/{idReserva}")
    Object buscarReserva(@PathVariable("idReserva") Long idReserva);

    @GetMapping("/reservas/pedido/{idPedidoRef}")
    List<?> buscarReservasPorPedido(@PathVariable("idPedidoRef") Long idPedidoRef);

    @PostMapping("/reservas/{idReserva}/confirmar")
    Object confirmarReserva(@PathVariable("idReserva") Long idReserva);

    @PostMapping("/reservas/{idReserva}/liberar")
    Object liberarReserva(@PathVariable("idReserva") Long idReserva);

    // Movimientos
    @GetMapping("/movimientos")
    List<?> listarMovimientos();

    @GetMapping("/movimientos/{idMovimiento}")
    Object buscarMovimiento(@PathVariable("idMovimiento") Long idMovimiento);

    @GetMapping("/movimientos/producto/{idProducto}")
    List<?> listarMovimientosPorProducto(@PathVariable("idProducto") Long idProducto);

    @GetMapping("/movimientos/bodega/{idBodega}")
    List<?> listarMovimientosPorBodega(@PathVariable("idBodega") Long idBodega);
}
