package org.fleetflow.souktransportbackend.service;

import org.fleetflow.souktransportbackend.dto.request.PaiementRequestDto;
import org.fleetflow.souktransportbackend.dto.response.PaiementDto;
import org.springframework.data.domain.Page;

public interface PaiementService {
    PaiementDto ajouterPaiement(PaiementRequestDto dto);
    PaiementDto consulterPaiement(Long id);
    PaiementDto modifierPaiement( Long id, PaiementRequestDto dto);
    void supprimerPaiement(Long id);
    Page<PaiementDto> listerPaiements(int page, int size);
    PaiementDto trouverParCargaison(Long cargaisonId);
}