package org.fleetflow.souktransportbackend.controller;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.response.DashboardAdminDto;
import org.fleetflow.souktransportbackend.dto.response.DashboardExpediteurDto;
import org.fleetflow.souktransportbackend.entity.User;
import org.fleetflow.souktransportbackend.repository.UserRepository;
import org.fleetflow.souktransportbackend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private  final UserRepository userRepository;

    @GetMapping("/admin")
    public ResponseEntity<DashboardAdminDto> getDashboardAdmin() {
        return ResponseEntity.ok(dashboardService.getDashboardAdmin());
    }

    @GetMapping("/expediteur")
    @PreAuthorize("hasRole('EXPEDITEUR')")
    public ResponseEntity<DashboardExpediteurDto> dashboardExpediteur(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
        DashboardExpediteurDto dashboard = dashboardService.dashboardExpediteur(user.getId());
        return ResponseEntity.ok(dashboard);
    }
}
