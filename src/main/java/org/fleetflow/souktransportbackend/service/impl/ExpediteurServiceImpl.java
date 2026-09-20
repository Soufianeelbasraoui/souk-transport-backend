package org.fleetflow.souktransportbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.response.ExpediteurDto;
import org.fleetflow.souktransportbackend.entity.Expediteur;
import org.fleetflow.souktransportbackend.repository.ExpediteurRepository;
import org.fleetflow.souktransportbackend.service.ExpediteurService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpediteurServiceImpl implements ExpediteurService {

    private final ExpediteurRepository expediteurRepository;

    @Override
    public ExpediteurDto getProfile(String email) {
        Expediteur expediteur = expediteurRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Expéditeur introuvable"));

        return new ExpediteurDto(
                expediteur.getId(),
                expediteur.getNom(),
                expediteur.getPrenom(),
                expediteur.getEmail(),
                expediteur.getTelephone(),
                expediteur.getVille(),
                expediteur.getRole(),
                expediteur.getStatutUser(),
                expediteur.getNomEntreprise(),
                expediteur.getAdresseEntreprise()
        );
    }
}
