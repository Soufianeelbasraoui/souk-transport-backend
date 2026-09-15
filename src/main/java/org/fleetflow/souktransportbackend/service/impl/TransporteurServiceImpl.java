package org.fleetflow.souktransportbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.response.TransporteurDto;
import org.fleetflow.souktransportbackend.entity.Transporteur;
import org.fleetflow.souktransportbackend.repository.TransporteurRepository;
import org.fleetflow.souktransportbackend.service.TransporteurService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransporteurServiceImpl implements TransporteurService {

    private final TransporteurRepository transporteurRepository;

    @Override
    public TransporteurDto getProfile(String email) {
        Transporteur transporteur =transporteurRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Transporteur introuvable"));

        return new TransporteurDto(
                transporteur.getId(),
                transporteur.getNom(),
                transporteur.getPrenom(),
                transporteur.getEmail(),
                transporteur.getTelephone(),
                transporteur.getVille(),
                transporteur.getRole(),
                transporteur.getStatutUser(),
                transporteur.getNumeroPermis(),
                transporteur.getCin()

        );
    }
}
