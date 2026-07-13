package com.smartlogix.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "MS-TRANSPORTISTAS-RUTAS")
public interface TransportistaClient {

    @GetMapping("/api/transportistas")
    List<Object> listarTransportistas();

    @GetMapping("/api/rutas")
    List<Object> listarRutas();

}
