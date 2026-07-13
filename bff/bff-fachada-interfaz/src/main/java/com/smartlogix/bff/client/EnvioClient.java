package com.smartlogix.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "MS-ENVIOS")
public interface EnvioClient {

    @GetMapping("/api/envios")
    List<Object> listarEnvios();

}
