package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bff")
public class PingController {

    @GetMapping("/ping")
    public ResponseEntity<ApiResponse> ping() {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("ms_bff operativo")
                .data("OK")
                .build());
    }
}
