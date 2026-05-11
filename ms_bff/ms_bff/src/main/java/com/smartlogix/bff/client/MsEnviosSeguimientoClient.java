package com.smartlogix.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Seguimiento en ms-envios está bajo el context path {@code /seguimientos}, no bajo {@code /api/envios}.
 */
@FeignClient(name = "ms-envios", contextId = "msEnviosSeguimiento", path = "/seguimientos")
public interface MsEnviosSeguimientoClient {

    @GetMapping("/envio/{idEnvio}")
    List<?> listarPorEnvio(@PathVariable("idEnvio") String idEnvio);
}
