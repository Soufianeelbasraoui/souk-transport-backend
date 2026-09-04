package org.fleetflow.souktransportbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.request.ExpediteurRequestDto;
import org.fleetflow.souktransportbackend.dto.request.LoginRequestDto;
import org.fleetflow.souktransportbackend.dto.request.RegisterRequest;
import org.fleetflow.souktransportbackend.dto.request.TransporteurRequestDto;
import org.fleetflow.souktransportbackend.dto.response.AuthResponseDto;
import org.fleetflow.souktransportbackend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/expediteur")
    public ResponseEntity<AuthResponseDto> registerExpediteur(@Valid @RequestBody ExpediteurRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerExpediteur(request));
    }

    @PostMapping("/register/transporteur")
    public ResponseEntity<AuthResponseDto> registerTransporteur(@Valid @RequestBody TransporteurRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerTransporteur(request));
    }
    @PostMapping("register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}