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
@FeignClient(name = "ms-pedidos", path = "/api/pedidos")
public interface PedidosClient {

    @GetMapping("/")
    List<?> listarPedidos();

    @PostMapping("/")
    Object crearPedido(@RequestBody Map<String, Object> request);

    @GetMapping("/{idPedido}")
    Object buscarPedido(@PathVariable("idPedido") Long idPedido);

    @PutMapping("/{idPedido}")
    Object actualizarPedido(@PathVariable("idPedido") Long idPedido,
                           @RequestBody Map<String, Object> request);

    @PatchMapping("/{idPedido}/cancelar")
    Object cancelarPedido(@PathVariable("idPedido") Long idPedido);

    @PatchMapping("/{idPedido}/estado")
    Object cambiarEstadoPedido(@PathVariable("idPedido") Long idPedido,
                              @RequestBody Map<String, Object> request);
}
