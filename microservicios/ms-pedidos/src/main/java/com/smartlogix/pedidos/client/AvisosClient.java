package com.smartlogix.pedidos.client;

import com.smartlogix.pedidos.dto.avisos.CrearAvisoRequest;
import com.smartlogix.pedidos.dto.avisos.AvisoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "avisosClient", url = "${avisos.service.url}")
public interface AvisosClient {

    @PostMapping("/api/avisos")
    AvisoResponse crearAviso(@RequestBody CrearAvisoRequest request);

}
