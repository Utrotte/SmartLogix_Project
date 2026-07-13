package com.smartlogix.pedidos.client;

import com.smartlogix.pedidos.dto.inventario.CrearReservaInventarioRequest;
import com.smartlogix.pedidos.dto.inventario.ReservaInventarioResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "MS-INVENTARIO", url = "${inventario.service.url:}")
public interface InventarioClient {

    @PostMapping("/api/inventario/reservas")
    ReservaInventarioResponse crearReserva(@RequestBody CrearReservaInventarioRequest request);

}
