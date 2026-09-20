package org.fleetflow.souktransportbackend.controller;

import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.response.ExpediteurDto;
import org.fleetflow.souktransportbackend.service.ExpediteurService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expediteurs")
@RequiredArgsConstructor
public class ExpediteurController {

    private final ExpediteurService expediteurService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('EXPEDITEUR')")
    public ResponseEntity<ExpediteurDto> getProfile(Authentication authentication) {
        return ResponseEntity.ok(expediteurService.getProfile(authentication.getName()));
    }
}
