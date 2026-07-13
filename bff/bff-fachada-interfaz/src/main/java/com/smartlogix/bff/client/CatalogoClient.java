package com.smartlogix.bff.client;

import com.smartlogix.bff.dto.ProductoResumenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "MS-CATALOGO-PRODUCTOS")
public interface CatalogoClient {

    @GetMapping("/api/catalogo/productos")
    List<ProductoResumenDTO> listarProductos();

}
