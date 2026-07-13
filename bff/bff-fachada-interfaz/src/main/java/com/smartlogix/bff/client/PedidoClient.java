package com.smartlogix.bff.client;

import com.smartlogix.bff.dto.PedidoResumenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "MS-PEDIDOS")
public interface PedidoClient {

    @GetMapping("/api/pedidos")
    List<PedidoResumenDTO> listarPedidos();

}
