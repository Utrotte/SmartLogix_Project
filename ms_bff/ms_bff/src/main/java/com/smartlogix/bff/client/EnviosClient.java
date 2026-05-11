package com.smartlogix.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Cliente Feign para el API principal de envíos bajo {@code /api/envios}.
 */
@FeignClient(name = "ms-envios", contextId = "msEnviosApi", path = "/api/envios")
public interface EnviosClient {

    @GetMapping
    List<?> listarEnvios();

    @PostMapping
    Object crearEnvio(@RequestBody Map<String, Object> request);

    @GetMapping("/{idEnvio}")
    Object buscarEnvio(@PathVariable("idEnvio") String idEnvio);

    @GetMapping("/pedido/{idPedidoRef}")
    Object buscarEnvioPorPedido(@PathVariable("idPedidoRef") String idPedidoRef);

    /**
     * ms-envios expone asignación como PUT con id de transportista en la ruta (no PATCH con body).
     */
    @PutMapping("/{idEnvio}/asignar-transportista/{idTransportista}")
    Object asignarTransportista(@PathVariable("idEnvio") String idEnvio,
                                @PathVariable("idTransportista") String idTransportista);

    @PutMapping("/{idEnvio}/estado")
    Object actualizarEstadoEnvio(@PathVariable("idEnvio") String idEnvio,
                                 @RequestBody Map<String, Object> request);
}
