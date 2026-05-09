package com.smartlogix.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Cliente Feign para el servicio de Envíos
 * Nombres de servicio registrados en Eureka:
 * - ms-envios (por defecto)
 * 
 * Si el nombre es distinto, cambiar en @FeignClient(name = "...")
 */
@FeignClient(name = "ms-envios", path = "/api/envios")
public interface EnviosClient {

    @GetMapping("/")
    List<?> listarEnvios();

    @PostMapping("/")
    Object crearEnvio(@RequestBody Map<String, Object> request);

    @GetMapping("/{idEnvio}")
    Object buscarEnvio(@PathVariable("idEnvio") Long idEnvio);

    @PutMapping("/{idEnvio}")
    Object actualizarEnvio(@PathVariable("idEnvio") Long idEnvio,
                          @RequestBody Map<String, Object> request);

    @PatchMapping("/{idEnvio}/transportista")
    Object asignarTransportista(@PathVariable("idEnvio") Long idEnvio,
                               @RequestBody Map<String, Object> request);

    @PatchMapping("/{idEnvio}/estado")
    Object actualizarEstadoEnvio(@PathVariable("idEnvio") Long idEnvio,
                                @RequestBody Map<String, Object> request);

    @GetMapping("/{idEnvio}/seguimiento")
    List<?> listarSeguimiento(@PathVariable("idEnvio") Long idEnvio);
}
