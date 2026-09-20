package org.fleetflow.souktransportbackend.service;

import org.fleetflow.souktransportbackend.dto.response.AdminDto;

public interface AdminService {
    AdminDto getProfile(String email);
}
