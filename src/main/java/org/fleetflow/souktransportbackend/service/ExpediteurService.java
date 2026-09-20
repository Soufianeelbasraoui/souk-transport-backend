package org.fleetflow.souktransportbackend.service;

import org.fleetflow.souktransportbackend.dto.response.ExpediteurDto;

public interface ExpediteurService {
    ExpediteurDto getProfile(String email);
}
