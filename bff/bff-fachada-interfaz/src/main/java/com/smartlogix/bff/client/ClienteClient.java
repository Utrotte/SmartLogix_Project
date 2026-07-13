package com.smartlogix.bff.client;

import com.smartlogix.bff.dto.ClienteResumenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "MS-CLIENTES")
public interface ClienteClient {

    @GetMapping("/api/clientes")
    List<ClienteResumenDTO> listarClientes();

}
