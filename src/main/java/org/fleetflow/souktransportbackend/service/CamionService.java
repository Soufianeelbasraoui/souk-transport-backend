package org.fleetflow.souktransportbackend.service;

import org.fleetflow.souktransportbackend.dto.request.CamionRequestDto;
import org.fleetflow.souktransportbackend.dto.response.CamionDto;
import org.fleetflow.souktransportbackend.enums.TypeCamion;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CamionService {
    CamionDto ajouterCamion(CamionRequestDto dto, String emailUserConnecte);
    CamionDto modifierCamion(Long id, CamionRequestDto dto);
    void supprimerCamion(Long id);
    CamionDto consulterCamion(Long id);
    List<CamionDto> listerParType(Long transporteurId, String type);
//    List<CamionDto> listerParCapaciteSuperieure(Long transporteurId, Double capacite);
//    Page<CamionDto> trierCamions(Long transporteurId, int page, int size, String sortBy, String direction);
    Page<CamionDto> mesCamions(int page, int size, String email);    Page<CamionDto> listerCamions(int page,int size);
    Page<CamionDto> rechercherParMarque(String marque,int page,int size);

    Page<CamionDto> rechercherMesCamionParMarque(String email, String marque, int page, int size);
}