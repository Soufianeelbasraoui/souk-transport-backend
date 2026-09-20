package org.fleetflow.souktransportbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.response.AdminDto;
import org.fleetflow.souktransportbackend.entity.Admin;
import org.fleetflow.souktransportbackend.repository.AdminRepository;
import org.fleetflow.souktransportbackend.service.AdminService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public AdminDto getProfile(String email) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));

        return new AdminDto(
                admin.getId(),
                admin.getNom(),
                admin.getPrenom(),
                admin.getEmail(),
                admin.getTelephone(),
                admin.getVille(),
                admin.getRole(),
                admin.getStatutUser()
        );
    }
}
