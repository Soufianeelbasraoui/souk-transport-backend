package org.fleetflow.souktransportbackend.service;

import org.fleetflow.souktransportbackend.dto.request.CargaisonRequestDto;
import org.fleetflow.souktransportbackend.dto.response.CargaisonDto;
import org.fleetflow.souktransportbackend.enums.StatutCargaison;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CargaisonService {
    CargaisonDto ajouterCargaison(CargaisonRequestDto dto,String email);
    CargaisonDto modifierCargaison(Long id, CargaisonRequestDto dto);
    void supprimerCargaison(Long id);
    CargaisonDto consulterCargaison(Long id);
    List<CargaisonDto> listerCargaisonsExpediteur(Long expediteurId);
    Page<CargaisonDto> listerCargaisons(int page, int size);
    List<CargaisonDto> listerParTrajet(Long trajetId);
//    Page<CargaisonDto> rechercher(String keyword, int page, int size);

    List<CargaisonDto> mesCargaisonsDisponibles(String email);
    Page<CargaisonDto> mesCargaisons(
            String email,
            StatutCargaison statut,
            int page,
            int size
    );


    Page<CargaisonDto> rechercherParDescription(String description,int page, int size);

    Page<CargaisonDto> filtrerParStatut(
            StatutCargaison statut,
            int page,
            int size
    );
}