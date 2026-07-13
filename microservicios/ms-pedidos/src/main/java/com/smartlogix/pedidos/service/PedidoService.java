package com.smartlogix.pedidos.service;

import com.smartlogix.pedidos.domain.DetallePedido;
import com.smartlogix.pedidos.domain.EstadoPedido;
import com.smartlogix.pedidos.domain.Pedido;
import com.smartlogix.pedidos.dto.*;
import com.smartlogix.pedidos.repository.DetallePedidoRepository;
import com.smartlogix.pedidos.client.InventarioClient;
import com.smartlogix.pedidos.dto.inventario.CrearReservaInventarioRequest;
import com.smartlogix.pedidos.dto.inventario.ReservaInventarioResponse;
import com.smartlogix.pedidos.repository.PedidoRepository;
import com.smartlogix.pedidos.client.AvisosClient;
import com.smartlogix.pedidos.dto.avisos.CrearAvisoRequest;
import com.smartlogix.pedidos.dto.avisos.AvisoResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detalleRepository;
    private final InventarioClient inventarioClient;
    private final AvisosClient avisosClient;

    public List<PedidoResponse> listar() {
        return pedidoRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public PedidoResponse obtener(Long id) {
        return pedidoRepository.findById(id).map(this::toResponse).orElse(null);
    }

    public List<PedidoResponse> porCliente(Long idCliente) {
        return pedidoRepository.findByIdCliente(idCliente).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public PedidoResponse crear(CrearPedidoRequest req) {
        Pedido pedido = Pedido.builder()
                .idCliente(req.getIdCliente())
                .estadoPedido(EstadoPedido.CREADO)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .activo(true)
                .total(BigDecimal.ZERO)
                .build();

        pedido = pedidoRepository.save(pedido);

        BigDecimal total = BigDecimal.ZERO;

        for (CrearDetallePedidoRequest d : req.getDetalles()) {
            if (d.getCantidad() == null || d.getCantidad() <= 0) throw new IllegalArgumentException("Cantidad debe ser mayor a 0");
            if (d.getPrecioUnitario() == null || d.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Precio unitario no puede ser negativo");

            BigDecimal subtotal = d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad()));
            Long idBodega = d.getIdBodega() != null ? d.getIdBodega() : 1L;
            DetallePedido detalle = DetallePedido.builder()
                    .idPedido(pedido.getId())
                    .idProducto(d.getIdProducto())
                    .idBodega(idBodega)
                    .cantidad(d.getCantidad())
                    .precioUnitario(d.getPrecioUnitario())
                    .subtotal(subtotal)
                    .build();
            detalleRepository.save(detalle);
            total = total.add(subtotal);

            // Call Inventario service to create reservation
            CrearReservaInventarioRequest reservaReq = CrearReservaInventarioRequest.builder()
                    .idProducto(detalle.getIdProducto())
                    .idBodega(detalle.getIdBodega())
                    .idPedido(pedido.getId())
                    .cantidadReservada(detalle.getCantidad())
                    .build();

            try {
                ReservaInventarioResponse resp = inventarioClient.crearReserva(reservaReq);
                if (resp == null) {
                    throw new RuntimeException("No se pudo reservar stock para el producto ID: " + detalle.getIdProducto());
                }
            } catch (Exception ex) {
                throw new RuntimeException("No se pudo reservar stock para el producto ID: " + detalle.getIdProducto(), ex);
            }
        }

        pedido.setTotal(total);
        pedido.setFechaActualizacion(LocalDateTime.now());
        pedido = pedidoRepository.save(pedido);

        // Registrar aviso en ms-integracion-avisos (no crítico)
        try {
            CrearAvisoRequest avisoReq = CrearAvisoRequest.builder()
                    .tipoAviso("PEDIDO_CREADO")
                    .canal("SISTEMA")
                    .destinatario("admin@smartlogix.cl")
                    .asunto("Pedido creado")
                    .mensaje("Se ha creado el pedido ID " + pedido.getId() + " para el cliente ID " + pedido.getIdCliente())
                    .idReferencia(pedido.getId())
                    .tipoReferencia("PEDIDO")
                    .build();

            AvisoResponse avisoResp = avisosClient.crearAviso(avisoReq);
        } catch (Exception ex) {
            System.out.println("No se pudo registrar aviso para el pedido ID: " + pedido.getId());
        }

        return toResponse(pedido);
    }

    @Transactional
    public PedidoResponse actualizarEstado(Long id, ActualizarEstadoPedidoRequest req) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
        pedido.setEstadoPedido(req.getEstadoPedido());
        pedido.setFechaActualizacion(LocalDateTime.now());
        pedido = pedidoRepository.save(pedido);
        return toResponse(pedido);
    }

    @Transactional
    public PedidoResponse cancelar(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
        pedido.setEstadoPedido(EstadoPedido.CANCELADO);
        pedido.setFechaActualizacion(LocalDateTime.now());
        pedido = pedidoRepository.save(pedido);
        return toResponse(pedido);
    }

    private PedidoResponse toResponse(Pedido p) {
        List<DetallePedidoResponse> detalles = detalleRepository.findByIdPedido(p.getId()).stream().map(d -> DetallePedidoResponse.builder()
                .id(d.getId())
                .idProducto(d.getIdProducto())
                .cantidad(d.getCantidad())
                .precioUnitario(d.getPrecioUnitario())
                .subtotal(d.getSubtotal())
                .build()).collect(Collectors.toList());

        return PedidoResponse.builder()
                .id(p.getId())
                .idCliente(p.getIdCliente())
                .estadoPedido(p.getEstadoPedido())
                .total(p.getTotal())
                .fechaCreacion(p.getFechaCreacion())
                .fechaActualizacion(p.getFechaActualizacion())
                .activo(p.isActivo())
                .detalles(detalles)
                .build();
    }

}
