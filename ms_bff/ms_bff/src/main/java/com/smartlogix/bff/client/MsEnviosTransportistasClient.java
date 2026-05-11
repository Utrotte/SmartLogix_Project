package com.smartlogix.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Transportistas en ms-envios están bajo {@code /transportistas}.
 */
@FeignClient(name = "ms-envios", contextId = "msEnviosTransportistas", path = "/transportistas")
public interface MsEnviosTransportistasClient {

    @GetMapping("/activos")
    List<?> listarActivos();
}
