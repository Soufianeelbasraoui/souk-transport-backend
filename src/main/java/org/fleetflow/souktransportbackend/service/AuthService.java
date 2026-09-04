package org.fleetflow.souktransportbackend.service;

import org.fleetflow.souktransportbackend.dto.request.ExpediteurRequestDto;
import org.fleetflow.souktransportbackend.dto.request.LoginRequestDto;
import org.fleetflow.souktransportbackend.dto.request.RegisterRequest;
import org.fleetflow.souktransportbackend.dto.request.TransporteurRequestDto;
import org.fleetflow.souktransportbackend.dto.response.AuthResponseDto;

public interface AuthService {
    AuthResponseDto registerExpediteur(ExpediteurRequestDto request);
    AuthResponseDto registerTransporteur(TransporteurRequestDto request);
    AuthResponseDto login(LoginRequestDto request);
    AuthResponseDto register(RegisterRequest request);
}