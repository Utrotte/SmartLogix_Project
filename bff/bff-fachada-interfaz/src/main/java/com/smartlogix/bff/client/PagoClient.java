package com.smartlogix.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "MS-PAGOS-VALIDACION")
public interface PagoClient {

    @GetMapping("/api/pagos")
    List<Object> listarPagos();

}
