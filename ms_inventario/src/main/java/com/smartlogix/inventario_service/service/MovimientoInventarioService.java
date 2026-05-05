package com.smartlogix.inventario_service.service;

import com.smartlogix.inventario_service.dto.response.MovimientoInventarioResponse;
import com.smartlogix.inventario_service.exception.ResourceNotFoundException;
import com.smartlogix.inventario_service.model.Bodega;
import com.smartlogix.inventario_service.model.MovimientoInventario;
import com.smartlogix.inventario_service.model.Producto;
import com.smartlogix.inventario_service.model.ReservaInventario;
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
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final BodegaRepository bodegaRepository;
    private final ReservaInventarioRepository reservaInventarioRepository;

    @Transactional(readOnly = true)
    public MovimientoInventarioResponse obtenerPorId(Long id) {
        return movimientoRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventarioResponse> listarTodos() {
        return movimientoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventarioResponse> obtenerPorProducto(Long idProducto) {
        if (!productoRepository.existsById(idProducto)) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + idProducto);
        }

        return movimientoRepository.findByProductoIdProducto(idProducto)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventarioResponse> obtenerPorBodega(Long idBodega) {
        if (!bodegaRepository.existsById(idBodega)) {
            throw new ResourceNotFoundException("Bodega no encontrada con ID: " + idBodega);
        }

        return movimientoRepository.findByBodegaIdBodega(idBodega)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Método interno para registrar movimientos (sin exposición como endpoint)
    @Transactional
    public void registrarMovimiento(Long idProducto, Long idBodega, Long idReserva,
                                    String tipoMovimiento, Integer cantidad,
                                    Integer stockResultante, String referencia, String observacion) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        Bodega bodega = bodegaRepository.findById(idBodega)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada"));

        ReservaInventario reserva = null;
        if (idReserva != null) {
            reserva = reservaInventarioRepository.findById(idReserva)
                    .orElse(null);
        }

        MovimientoInventario movimiento = MovimientoInventario.builder()
                .producto(producto)
                .bodega(bodega)
                .reserva(reserva)
                .tipoMovimiento(tipoMovimiento)
                .cantidad(cantidad)
                .stockResultante(stockResultante)
                .referencia(referencia)
                .observacion(observacion)
                .build();

        movimientoRepository.save(movimiento);
    }

    private MovimientoInventarioResponse mapToResponse(MovimientoInventario movimiento) {
        return MovimientoInventarioResponse.builder()
                .idMovimiento(movimiento.getIdMovimiento())
                .idProducto(movimiento.getProducto().getIdProducto())
                .codigoSkuProducto(movimiento.getProducto().getCodigoSku())
                .nombreProducto(movimiento.getProducto().getNombre())
                .idBodega(movimiento.getBodega().getIdBodega())
                .nombreBodega(movimiento.getBodega().getNombre())
                .idReserva(movimiento.getReserva() != null ? movimiento.getReserva().getIdReserva() : null)
                .tipoMovimiento(movimiento.getTipoMovimiento())
                .cantidad(movimiento.getCantidad())
                .stockResultante(movimiento.getStockResultante())
                .referencia(movimiento.getReferencia())
                .fechaMovimiento(movimiento.getFechaMovimiento())
                .observacion(movimiento.getObservacion())
                .build();
    }
}
