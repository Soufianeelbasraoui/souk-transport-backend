package org.fleetflow.souktransportbackend.controller;


import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.response.DashboardAdminDto;
import org.fleetflow.souktransportbackend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/admin")
    public ResponseEntity<DashboardAdminDto> getDashboardAdmin() {
        return ResponseEntity.ok(dashboardService.getDashboardAdmin());
    }
}
