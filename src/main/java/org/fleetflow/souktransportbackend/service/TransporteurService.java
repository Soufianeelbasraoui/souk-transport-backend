package org.fleetflow.souktransportbackend.service;

import org.fleetflow.souktransportbackend.dto.response.TransporteurDto;

public interface TransporteurService {
    TransporteurDto getProfile(String email);
}
