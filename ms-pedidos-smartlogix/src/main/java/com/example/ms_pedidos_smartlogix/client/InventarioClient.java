package com.example.ms_pedidos_smartlogix.client;

import com.example.ms_pedidos_smartlogix.config.FeignConfig;
import com.example.ms_pedidos_smartlogix.integration.dto.ReservaInventarioRequestDTO;
import com.example.ms_pedidos_smartlogix.integration.dto.ReservaInventarioResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "ms-inventario",
    configuration = FeignConfig.class
)
public interface InventarioClient {

    @PostMapping("/api/inventario/reservas")
    ReservaInventarioResponseDTO crearReserva(@RequestBody ReservaInventarioRequestDTO requestDTO);
}
