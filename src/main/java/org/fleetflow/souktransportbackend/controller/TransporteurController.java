package org.fleetflow.souktransportbackend.controller;

import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.response.TransporteurDto;
import org.fleetflow.souktransportbackend.service.TransporteurService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transporteurs")
@RequiredArgsConstructor
public class TransporteurController {

    private final TransporteurService transporteurService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('TRANSPORTEUR')")
    public ResponseEntity<TransporteurDto> getProfile(Authentication authentication) {
        return ResponseEntity.ok(transporteurService.getProfile(authentication.getName()));
    }
}