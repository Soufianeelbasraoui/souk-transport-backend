package org.fleetflow.souktransportbackend.service;

import org.fleetflow.souktransportbackend.dto.request.CargaisonRequestDto;
import org.fleetflow.souktransportbackend.dto.response.CargaisonDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CargaisonService {
    CargaisonDto ajouterCargaison(CargaisonRequestDto dto);
    CargaisonDto modifierCargaison(Long id, CargaisonRequestDto dto);
    void supprimerCargaison(Long id);
    CargaisonDto consulterCargaison(Long id);
    List<CargaisonDto> listerCargaisons();
    List<CargaisonDto> listerCargaisonsExpediteur(Long expediteurId);
    Page<CargaisonDto> listerCargaisons(int page, int size);
    List<CargaisonDto> listerParTrajet(Long trajetId);
    Page<CargaisonDto> rechercher(String keyword, int page, int size);
}