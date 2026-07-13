package com.smartlogix.bff.client;

import com.smartlogix.bff.dto.AvisoResumenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "avisosClient", url = "${avisos.service.url}")
public interface AvisosClient {

    @GetMapping("/api/avisos")
    List<AvisoResumenDTO> listarAvisos();

}
