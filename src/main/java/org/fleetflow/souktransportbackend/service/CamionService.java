package org.fleetflow.souktransportbackend.service;

import org.fleetflow.souktransportbackend.dto.request.CamionRequestDto;
import org.fleetflow.souktransportbackend.dto.response.CamionDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CamionService {
    CamionDto ajouterCamion(CamionRequestDto dto);
    CamionDto modifierCamion(Long id, CamionRequestDto dto);
    void supprimerCamion(Long id);
    CamionDto consulterCamion(Long id);
    List<CamionDto> listerCamionsTransporteur(Long transporteurId);
    Page<CamionDto> listerCamionsTransporteur(Long transporteurId, int page, int size);
    List<CamionDto> listerParType(Long transporteurId, String type);
    List<CamionDto> listerParCapaciteSuperieure(Long transporteurId, Double capacite);
    Page<CamionDto> rechercher(Long transporteurId, String keyword, int page, int size);
    Page<CamionDto> trierCamions(Long transporteurId, int page, int size, String sortBy, String direction);
}