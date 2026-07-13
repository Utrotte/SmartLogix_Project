package com.smartlogix.bff.client;

import com.smartlogix.bff.dto.ExistenciaInventarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "MS-INVENTARIO")
public interface InventarioClient {

    @GetMapping("/api/inventario/existencias")
    List<ExistenciaInventarioDTO> listarExistencias();

}
