package com.smartlogix.bff.client;

import com.smartlogix.bff.dto.BodegaResumenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "MS-BODEGAS-UBICACIONES")
public interface BodegaClient {

    @GetMapping("/api/bodegas")
    List<BodegaResumenDTO> listarBodegas();

}
