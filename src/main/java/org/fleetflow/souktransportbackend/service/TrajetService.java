package org.fleetflow.souktransportbackend.service;

import org.fleetflow.souktransportbackend.dto.request.TrajetRequestDto;
import org.fleetflow.souktransportbackend.dto.response.TrajetDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;

public interface TrajetService {

    TrajetDto ajouterTrajet(TrajetRequestDto dto);
    TrajetDto modifierTrajet(Long id, TrajetRequestDto dto);

    void supprimerTrajet(Long id);
    TrajetDto consulterTrajet(Long id);
    Page<TrajetDto> listerTrajets(int page, int size);
    List<TrajetDto> listerTrajetsPublies();
    Page<TrajetDto> trierTrajets(int page, int size, String sortBy, String direction);
   Page<TrajetDto> RecentTrajets(int page ,int size);
}