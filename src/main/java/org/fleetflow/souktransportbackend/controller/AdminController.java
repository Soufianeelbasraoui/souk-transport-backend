package org.fleetflow.souktransportbackend.controller;

import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.response.AdminDto;
import org.fleetflow.souktransportbackend.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDto> getProfile(Authentication authentication) {
        return ResponseEntity.ok(adminService.getProfile(authentication.getName()));
    }
}
