package org.fleetflow.souktransportbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.request.TrajetRequestDto;
import org.fleetflow.souktransportbackend.dto.response.TrajetDto;
import org.fleetflow.souktransportbackend.entity.Camion;
import org.fleetflow.souktransportbackend.entity.Trajet;
import org.fleetflow.souktransportbackend.enums.StatutTrajet;
import org.fleetflow.souktransportbackend.mapper.TrajetMapper;
import org.fleetflow.souktransportbackend.repository.CamionRepository;
import org.fleetflow.souktransportbackend.repository.TrajetRepository;
import org.fleetflow.souktransportbackend.service.TrajetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrajetServiceImpl implements TrajetService {

    private final TrajetRepository trajetRepository;
    private final CamionRepository camionRepository;
    private final TrajetMapper trajetMapper;

    @Override
    public TrajetDto ajouterTrajet(TrajetRequestDto dto) {

        Camion camion = camionRepository.findById(dto.getCamionId()).orElseThrow(() -> new EntityNotFoundException("Camion introuvable avec l'id : " + dto.getCamionId()));
        if (!Boolean.TRUE.equals(camion.getDisponible())) {
            throw new RuntimeException("Le camion sélectionné n'est pas disponible.");
        }

        Trajet trajet = trajetMapper.toEntityRequest(dto);
        trajet.setCamion(camion);
        if (dto.getStatutTrajet() == null) {
            trajet.setStatutTrajet(StatutTrajet.PUBLIE);
        }
        return trajetMapper.toDto(trajetRepository.save(trajet));
    }

    @Override
    public TrajetDto modifierTrajet(Long id, TrajetRequestDto dto) {
        Trajet trajet = trajetRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Trajet introuvable avec l'id : " + id));
        if (dto.getCamionId() != null) {
            Camion camion = camionRepository.findById(dto.getCamionId()).orElseThrow(() -> new EntityNotFoundException("Camion introuvable avec l'id : " + dto.getCamionId()));
            if (!Boolean.TRUE.equals(camion.getDisponible())) {
                throw new RuntimeException("Le camion sélectionné n'est pas disponible.");
            }
            trajet.setCamion(camion);
        }
        trajetMapper.updateEntityFromDto(dto, trajet);
        return trajetMapper.toDto(trajetRepository.save(trajet));
    }

    @Override
    public void supprimerTrajet(Long id) {
        if (!trajetRepository.existsById(id)) {
            throw new EntityNotFoundException("Trajet introuvable avec l'id : " + id);
        }
        trajetRepository.deleteById(id);
    }

    @Override
    public TrajetDto consulterTrajet(Long id) {
        Trajet trajet = trajetRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Trajet introuvable avec l'id : " + id));
        return trajetMapper.toDto(trajet);
    }

    @Override
    public Page<TrajetDto> listerTrajets(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return trajetRepository.findAll(pageable).map(trajetMapper::toDto);
    }

    @Override
    public List<TrajetDto> listerTrajetsPublies() {
        return trajetMapper.toDtoList(trajetRepository.findByStatutTrajet(StatutTrajet.PUBLIE));
    }

    @Override
    public Page<TrajetDto> trierTrajets(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return trajetRepository.findAll(pageable).map(trajetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrajetDto> RecentTrajets(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return trajetRepository.findByStatutTrajetOrderByDateDepartDesc(StatutTrajet.PUBLIE, pageable).map(trajetMapper::toDto);
    }
}