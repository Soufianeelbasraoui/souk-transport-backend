package org.fleetflow.souktransportbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.fleetflow.souktransportbackend.dto.request.TrajetRequestDto;
import org.fleetflow.souktransportbackend.dto.response.TrajetDto;
import org.fleetflow.souktransportbackend.entity.Camion;
import org.fleetflow.souktransportbackend.entity.Trajet;
import org.fleetflow.souktransportbackend.entity.User;
import org.fleetflow.souktransportbackend.enums.StatutTrajet;
import org.fleetflow.souktransportbackend.mapper.TrajetMapper;
import org.fleetflow.souktransportbackend.repository.CamionRepository;
import org.fleetflow.souktransportbackend.repository.TrajetRepository;
import org.fleetflow.souktransportbackend.repository.UserRepository;
import org.fleetflow.souktransportbackend.service.TrajetService;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrajetServiceImpl implements TrajetService {

    private final TrajetRepository trajetRepository;
    private final CamionRepository camionRepository;
    private final UserRepository userRepository;
    private final TrajetMapper trajetMapper;

    @Override

    public TrajetDto ajouterTrajet(TrajetRequestDto dto) {
        Camion camion = camionRepository.findById(dto.getCamionId()).orElseThrow(() -> new EntityNotFoundException("Camion introuvable avec l'id : " + dto.getCamionId()));
        if (!Boolean.TRUE.equals(camion.getDisponible())) {
            throw new RuntimeException("Le camion sélectionné n'est pas disponible.");
        }
        Trajet trajet = trajetMapper.toEntityRequest(dto);
        trajet.setCamion(camion);
        if (trajet.getStatutTrajet()==null) {
            trajet.setStatutTrajet(StatutTrajet.PUBLIE);
        }
        Trajet trajetSaved = trajetRepository.save(trajet);
        return trajetMapper.toDto(trajetSaved);
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

        Trajet trajetUpdated = trajetRepository.save(trajet);

        return trajetMapper.toDto(trajetUpdated);
    }

    @Override
    public void supprimerTrajet(Long id) {
        if (!trajetRepository.existsById(id)) {
            throw new EntityNotFoundException("Trajet introuvable avec l'id : " + id);
        }
        trajetRepository.deleteById(id);
    }
    @Override
    @Transactional(readOnly = true)
    public TrajetDto consulterTrajet(Long id) {
        Trajet trajet = trajetRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Trajet introuvable"));
        TrajetDto dto = trajetMapper.toDto(trajet);
        Long nombreReservations = trajetRepository.countReservationsByTrajetId(trajet.getId());
        dto.setNombreReservations(nombreReservations.intValue());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrajetDto> listerTrajets(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return trajetRepository.findAll(pageable).map(trajet -> {TrajetDto dto = trajetMapper.toDto(trajet);
                    Long nombreReservations = trajetRepository.countReservationsByTrajetId(trajet.getId());
                    dto.setNombreReservations(nombreReservations.intValue());
                    return dto;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrajetDto> listerTrajetsPublies() {
        return trajetRepository.findByStatutTrajet(StatutTrajet.PUBLIE).stream().map(trajetMapper::toDto).toList();
    }
    @Override
    @Transactional(readOnly = true)
    public List<TrajetDto> mesTrajets(String email) {
        User transporteur = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
        List<Trajet> trajets = trajetRepository.findByCamionTransporteurId(transporteur.getId());
        List<TrajetDto> result = new ArrayList<>();
        for (Trajet trajet : trajets) {
            TrajetDto dto = trajetMapper.toDto(trajet);
            Long nombreReservations = trajetRepository.countReservationsByTrajetId(trajet.getId());
            dto.setNombreReservations(nombreReservations.intValue());
            result.add(dto);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrajetDto> recentTrajets(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return trajetRepository.findByStatutTrajetOrderByDateDepartDesc(StatutTrajet.PUBLIE, pageable).map(trajetMapper::toDto);
    }

    @Override
    public Long countTrajet(String email) {
        User transporteur = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
        return trajetRepository.countByCamionTransporteurId(transporteur.getId());
    }

    @Override
    public Page<TrajetDto> rechercher(String recherche, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return trajetRepository.findByVilleDepartContainingIgnoreCaseOrVilleArriveeContainingIgnoreCase(recherche, recherche, pageable).map(trajetMapper::toDto);
    }
}