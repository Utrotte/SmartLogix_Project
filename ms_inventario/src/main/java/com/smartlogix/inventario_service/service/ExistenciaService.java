package com.smartlogix.inventario_service.service;

import com.smartlogix.inventario_service.dto.request.ExistenciaRequest;
import com.smartlogix.inventario_service.dto.response.ExistenciaResponse;
import com.smartlogix.inventario_service.exception.BusinessException;
import com.smartlogix.inventario_service.exception.ResourceNotFoundException;
import com.smartlogix.inventario_service.model.Bodega;
import com.smartlogix.inventario_service.model.Existencia;
import com.smartlogix.inventario_service.model.Producto;
import com.smartlogix.inventario_service.repository.BodegaRepository;
import com.smartlogix.inventario_service.repository.ExistenciaRepository;
import com.smartlogix.inventario_service.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExistenciaService {

    private final ExistenciaRepository existenciaRepository;
    private final ProductoRepository productoRepository;
    private final BodegaRepository bodegaRepository;

    @Transactional(readOnly = true)
    public ExistenciaResponse obtenerPorId(Long id) {
        return existenciaRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Existencia no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<ExistenciaResponse> obtenerPorProducto(Long idProducto) {
        // Validar que el producto exista
        if (!productoRepository.existsById(idProducto)) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + idProducto);
        }

        return existenciaRepository.findByProductoIdProducto(idProducto)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExistenciaResponse> obtenerPorBodega(Long idBodega) {
        // Validar que la bodega exista
        if (!bodegaRepository.existsById(idBodega)) {
            throw new ResourceNotFoundException("Bodega no encontrada con ID: " + idBodega);
        }

        return existenciaRepository.findByBodegaIdBodega(idBodega)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ExistenciaResponse obtenerPorProductoYBodega(Long idProducto, Long idBodega) {
        return existenciaRepository.findByProductoIdProductoAndBodegaIdBodega(idProducto, idBodega)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe stock para el producto " + idProducto + " en la bodega " + idBodega));
    }

    public ExistenciaResponse crear(ExistenciaRequest request) {
        Producto producto = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + request.getIdProducto()));

        Bodega bodega = bodegaRepository.findById(request.getIdBodega())
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + request.getIdBodega()));

        // Validar que no exista una existencia previa
        if (existenciaRepository.findByProductoIdProductoAndBodegaIdBodega(
                request.getIdProducto(), request.getIdBodega()).isPresent()) {
            throw new BusinessException("Ya existe un registro de stock para este producto en esta bodega");
        }

        Integer stockActual = request.getStockActual() != null ? request.getStockActual() : 0;
        Integer stockMinimo = request.getStockMinimo() != null ? request.getStockMinimo() : 0;

        Existencia existencia = Existencia.builder()
                .producto(producto)
                .bodega(bodega)
                .stockActual(stockActual)
                .stockReservado(0)
                .stockMinimo(stockMinimo)
                .build();

        existencia.recalcularStockDisponible();
        Existencia existenciaSaved = existenciaRepository.save(existencia);
        return mapToResponse(existenciaSaved);
    }

    public ExistenciaResponse ajustarStock(Long idExistencia, Integer ajuste, String observacion) {
        Existencia existencia = existenciaRepository.findById(idExistencia)
                .orElseThrow(() -> new ResourceNotFoundException("Existencia no encontrada con ID: " + idExistencia));

        Integer nuevoStock = existencia.getStockActual() + ajuste;
        if (nuevoStock < 0) {
            throw new BusinessException("No se puede restar más stock del disponible. Stock actual: " + 
                    existencia.getStockActual() + ", ajuste solicitado: " + ajuste);
        }

        existencia.setStockActual(nuevoStock);
        existencia.recalcularStockDisponible();
        existencia.setFechaActualizacion(LocalDateTime.now());

        Existencia existenciaUpdated = existenciaRepository.save(existencia);
        return mapToResponse(existenciaUpdated);
    }

    private ExistenciaResponse mapToResponse(Existencia existencia) {
        return ExistenciaResponse.builder()
                .idExistencia(existencia.getIdExistencia())
                .idProducto(existencia.getProducto().getIdProducto())
                .codigoSkuProducto(existencia.getProducto().getCodigoSku())
                .nombreProducto(existencia.getProducto().getNombre())
                .idBodega(existencia.getBodega().getIdBodega())
                .nombreBodega(existencia.getBodega().getNombre())
                .stockActual(existencia.getStockActual())
                .stockReservado(existencia.getStockReservado())
                .stockDisponible(existencia.getStockDisponible())
                .stockMinimo(existencia.getStockMinimo())
                .fechaActualizacion(existencia.getFechaActualizacion())
                .build();
    }
}
