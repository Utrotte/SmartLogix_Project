package com.smartlogix.inventario_service.service;

import com.smartlogix.inventario_service.dto.request.ReservarStockRequest;
import com.smartlogix.inventario_service.dto.response.ReservaInventarioResponse;
import com.smartlogix.inventario_service.dto.response.ReservaInventarioDetalleResponse;
import com.smartlogix.inventario_service.exception.BusinessException;
import com.smartlogix.inventario_service.exception.ResourceNotFoundException;
import com.smartlogix.inventario_service.model.*;
import com.smartlogix.inventario_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservaInventarioService {

    private final ReservaInventarioRepository reservaInventarioRepository;
    private final ReservaInventarioDetalleRepository reservaDetalleRepository;
    private final ProductoRepository productoRepository;
    private final BodegaRepository bodegaRepository;
    private final ExistenciaRepository existenciaRepository;
    private final MovimientoInventarioService movimientoInventarioService;

    @Transactional(readOnly = true)
    public ReservaInventarioResponse obtenerPorId(Long id) {
        return reservaInventarioRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<ReservaInventarioResponse> obtenerPorPedido(Long idPedidoRef) {
        return reservaInventarioRepository.findByIdPedidoRef(idPedidoRef)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservaInventarioResponse> listarTodas() {
        return reservaInventarioRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ReservaInventarioResponse reservarStock(ReservarStockRequest request) {
        // Validar que todos los productos y bodegas existan
        request.getDetalles().forEach(detalle -> {
            if (!productoRepository.existsById(detalle.getIdProducto())) {
                throw new ResourceNotFoundException("Producto no encontrado con ID: " + detalle.getIdProducto());
            }
            if (!bodegaRepository.existsById(detalle.getIdBodega())) {
                throw new ResourceNotFoundException("Bodega no encontrada con ID: " + detalle.getIdBodega());
            }
        });

        // Validar stock disponible para todos los detalles
        for (var detalle : request.getDetalles()) {
            Existencia existencia = existenciaRepository
                    .findByProductoIdProductoAndBodegaIdBodega(detalle.getIdProducto(), detalle.getIdBodega())
                    .orElseThrow(() -> new BusinessException(
                            "No existe stock registrado para el producto " + detalle.getIdProducto() +
                                    " en la bodega " + detalle.getIdBodega()));

            if (existencia.getStockDisponible() < detalle.getCantidadReservada()) {
                throw new BusinessException(
                        "Stock insuficiente para el producto " + existencia.getProducto().getNombre() +
                                ". Disponible: " + existencia.getStockDisponible() +
                                ", Solicitado: " + detalle.getCantidadReservada());
            }
        }

        // Crear la reserva
        ReservaInventario reserva = ReservaInventario.builder()
                .idPedidoRef(request.getIdPedidoRef())
                .codigoPedidoRef(request.getCodigoPedidoRef())
                .estadoReserva("CREADA")
                .fechaExpiracion(request.getFechaExpiracion())
                .build();

        ReservaInventario reservaSaved = reservaInventarioRepository.save(reserva);

        // Crear los detalles de la reserva y actualizar el stock
        for (var detalleRequest : request.getDetalles()) {
            Producto producto = productoRepository.findById(detalleRequest.getIdProducto())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            Bodega bodega = bodegaRepository.findById(detalleRequest.getIdBodega())
                    .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada"));

            Existencia existencia = existenciaRepository
                    .findByProductoIdProductoAndBodegaIdBodega(detalleRequest.getIdProducto(), detalleRequest.getIdBodega())
                    .orElseThrow(() -> new BusinessException("Existencia no encontrada"));

            // Crear detalle de reserva
            ReservaInventarioDetalle detalle = ReservaInventarioDetalle.builder()
                    .reserva(reservaSaved)
                    .producto(producto)
                    .bodega(bodega)
                    .cantidadReservada(detalleRequest.getCantidadReservada())
                    .estadoDetalle("RESERVADO")
                    .build();

            reservaDetalleRepository.save(detalle);

            // Actualizar stock: aumentar reservado y recalcular disponible
            existencia.setStockReservado(existencia.getStockReservado() + detalleRequest.getCantidadReservada());
            existencia.recalcularStockDisponible();
            existencia.setFechaActualizacion(LocalDateTime.now());
            existenciaRepository.save(existencia);

            // Registrar movimiento de tipo RESERVA
            movimientoInventarioService.registrarMovimiento(
                    producto.getIdProducto(),
                    bodega.getIdBodega(),
                    reservaSaved.getIdReserva(),
                    "RESERVA",
                    detalleRequest.getCantidadReservada(),
                    existencia.getStockActual(),
                    "PED-" + request.getCodigoPedidoRef(),
                    "Reserva de stock para pedido"
            );
        }

        return mapToResponse(reservaSaved);
    }

    public ReservaInventarioResponse confirmarReserva(Long idReserva, String observacion) {
        ReservaInventario reserva = reservaInventarioRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + idReserva));

        if (!reserva.getEstadoReserva().equals("CREADA")) {
            throw new BusinessException("Solo se pueden confirmar reservas en estado CREADA. Estado actual: " + reserva.getEstadoReserva());
        }

        List<ReservaInventarioDetalle> detalles = reservaDetalleRepository.findAll()
                .stream()
                .filter(d -> d.getReserva().getIdReserva().equals(idReserva))
                .collect(Collectors.toList());

        // Procesar cada detalle
        for (ReservaInventarioDetalle detalle : detalles) {
            Existencia existencia = existenciaRepository
                    .findByProductoIdProductoAndBodegaIdBodega(
                            detalle.getProducto().getIdProducto(),
                            detalle.getBodega().getIdBodega())
                    .orElseThrow(() -> new BusinessException("Existencia no encontrada"));

            // Descontar del stock actual y del stock reservado
            existencia.setStockActual(existencia.getStockActual() - detalle.getCantidadReservada());
            existencia.setStockReservado(existencia.getStockReservado() - detalle.getCantidadReservada());
            existencia.recalcularStockDisponible();
            existencia.setFechaActualizacion(LocalDateTime.now());
            existenciaRepository.save(existencia);

            // Actualizar estado del detalle
            detalle.setEstadoDetalle("DESCONTADO");
            reservaDetalleRepository.save(detalle);

            // Registrar movimiento de tipo SALIDA
            movimientoInventarioService.registrarMovimiento(
                    detalle.getProducto().getIdProducto(),
                    detalle.getBodega().getIdBodega(),
                    reserva.getIdReserva(),
                    "SALIDA",
                    detalle.getCantidadReservada(),
                    existencia.getStockActual(),
                    "PED-" + reserva.getCodigoPedidoRef(),
                    observacion != null ? observacion : "Confirmación de reserva para pedido"
            );
        }

        // Actualizar estado de la reserva
        reserva.setEstadoReserva("CONFIRMADA");
        ReservaInventario reservaUpdated = reservaInventarioRepository.save(reserva);

        return mapToResponse(reservaUpdated);
    }

    public ReservaInventarioResponse liberarReserva(Long idReserva, String observacion) {
        ReservaInventario reserva = reservaInventarioRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + idReserva));

        if (!reserva.getEstadoReserva().equals("CREADA")) {
            throw new BusinessException("Solo se pueden liberar reservas en estado CREADA. Estado actual: " + reserva.getEstadoReserva());
        }

        List<ReservaInventarioDetalle> detalles = reservaDetalleRepository.findAll()
                .stream()
                .filter(d -> d.getReserva().getIdReserva().equals(idReserva))
                .collect(Collectors.toList());

        // Procesar cada detalle
        for (ReservaInventarioDetalle detalle : detalles) {
            Existencia existencia = existenciaRepository
                    .findByProductoIdProductoAndBodegaIdBodega(
                            detalle.getProducto().getIdProducto(),
                            detalle.getBodega().getIdBodega())
                    .orElseThrow(() -> new BusinessException("Existencia no encontrada"));

            // Disminuir solo el stock reservado
            existencia.setStockReservado(existencia.getStockReservado() - detalle.getCantidadReservada());
            existencia.recalcularStockDisponible();
            existencia.setFechaActualizacion(LocalDateTime.now());
            existenciaRepository.save(existencia);

            // Actualizar estado del detalle
            detalle.setEstadoDetalle("LIBERADO");
            reservaDetalleRepository.save(detalle);

            // Registrar movimiento de tipo LIBERACION
            movimientoInventarioService.registrarMovimiento(
                    detalle.getProducto().getIdProducto(),
                    detalle.getBodega().getIdBodega(),
                    reserva.getIdReserva(),
                    "LIBERACION",
                    detalle.getCantidadReservada(),
                    existencia.getStockActual(),
                    "PED-" + reserva.getCodigoPedidoRef(),
                    observacion != null ? observacion : "Liberación de reserva"
            );
        }

        // Actualizar estado de la reserva
        reserva.setEstadoReserva("LIBERADA");
        ReservaInventario reservaUpdated = reservaInventarioRepository.save(reserva);

        return mapToResponse(reservaUpdated);
    }

    private ReservaInventarioResponse mapToResponse(ReservaInventario reserva) {
        List<ReservaInventarioDetalle> detalles = reservaDetalleRepository.findAll()
                .stream()
                .filter(d -> d.getReserva().getIdReserva().equals(reserva.getIdReserva()))
                .collect(Collectors.toList());

        List<ReservaInventarioDetalleResponse> detallesResponse = detalles.stream()
                .map(detalle -> ReservaInventarioDetalleResponse.builder()
                        .idReservaDetalle(detalle.getIdReservaDetalle())
                        .idReserva(detalle.getReserva().getIdReserva())
                        .idProducto(detalle.getProducto().getIdProducto())
                        .codigoSkuProducto(detalle.getProducto().getCodigoSku())
                        .nombreProducto(detalle.getProducto().getNombre())
                        .idBodega(detalle.getBodega().getIdBodega())
                        .nombreBodega(detalle.getBodega().getNombre())
                        .cantidadReservada(detalle.getCantidadReservada())
                        .estadoDetalle(detalle.getEstadoDetalle())
                        .build())
                .collect(Collectors.toList());

        return ReservaInventarioResponse.builder()
                .idReserva(reserva.getIdReserva())
                .idPedidoRef(reserva.getIdPedidoRef())
                .codigoPedidoRef(reserva.getCodigoPedidoRef())
                .estadoReserva(reserva.getEstadoReserva())
                .fechaReserva(reserva.getFechaReserva())
                .fechaExpiracion(reserva.getFechaExpiracion())
                .detalles(detallesResponse)
                .build();
    }
}
