package org.fleetflow.souktransportbackend.service;

import org.fleetflow.souktransportbackend.dto.request.TrajetRequestDto;
import org.fleetflow.souktransportbackend.dto.response.TrajetDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TrajetService {
    TrajetDto ajouterTrajet(TrajetRequestDto dto);

    TrajetDto modifierTrajet(Long id, TrajetRequestDto dto);

    void supprimerTrajet(Long id);
    TrajetDto consulterTrajet(Long id);
    Page<TrajetDto> listerTrajets(int page, int size);

    List<TrajetDto> listerTrajetsPublies();
    List<TrajetDto> mesTrajets(String email);
    Page<TrajetDto> recentTrajets(int page, int size);
    Long countTrajet(String email);
}