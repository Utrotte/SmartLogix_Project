package com.smartlogix.pedidos.init;

import com.smartlogix.pedidos.dto.CrearDetallePedidoRequest;
import com.smartlogix.pedidos.dto.CrearPedidoRequest;
import com.smartlogix.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PedidoService pedidoService;

    @Override
    public void run(String... args) throws Exception {
        // Create initial pedido with idCliente=1 if none exist
        CrearDetallePedidoRequest d = CrearDetallePedidoRequest.builder()
            .idProducto(1L)
            .idBodega(1L)
            .cantidad(2)
            .precioUnitario(BigDecimal.valueOf(15000))
            .build();

        CrearPedidoRequest req = CrearPedidoRequest.builder()
                .idCliente(1L)
                .detalles(List.of(d))
                .build();

        // Wrap in try to avoid failing startup if already exists
        try {
            pedidoService.crear(req);
        } catch (Exception ignored) {
        }
    }
}
