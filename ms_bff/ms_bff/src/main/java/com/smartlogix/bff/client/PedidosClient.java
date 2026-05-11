package com.smartlogix.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Cliente Feign para el servicio de Pedidos
 * Nombres de servicio registrados en Eureka:
 * - ms-pedidos (por defecto)
 * 
 * Si el nombre es distinto, cambiar en @FeignClient(name = "...")
 */
@FeignClient(name = "ms-pedidos-smartlogix", path = "/api/pedidos")
public interface PedidosClient {

    @GetMapping
    List<?> listarPedidos();

    @PostMapping
    Object crearPedido(@RequestBody Map<String, Object> request);

    @GetMapping("/{idPedido}")
    Object buscarPedido(@PathVariable("idPedido") Long idPedido);

    @GetMapping("/cliente/{idCliente}")
    List<?> listarPedidosPorCliente(@PathVariable("idCliente") Long idCliente);

    @PutMapping("/{idPedido}")
    Object actualizarPedido(@PathVariable("idPedido") Long idPedido,
                           @RequestBody Map<String, Object> request);

    @PutMapping("/{idPedido}/cancelar")
    Object cancelarPedido(@PathVariable("idPedido") Long idPedido);

    @PutMapping("/{idPedido}/estado")
    Object cambiarEstadoPedido(@PathVariable("idPedido") Long idPedido,
                              @RequestBody Map<String, Object> request);

    @DeleteMapping("/{idPedido}")
    void eliminarPedido(@PathVariable("idPedido") Long idPedido);
}
