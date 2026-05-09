package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.response.DashboardResumenResponse;
import com.smartlogix.bff.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bff/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/resumen")
    public ResponseEntity<DashboardResumenResponse> obtenerResumen() {
        DashboardResumenResponse response = dashboardService.obtenerResumen();
        return ResponseEntity.ok(response);
    }
}
