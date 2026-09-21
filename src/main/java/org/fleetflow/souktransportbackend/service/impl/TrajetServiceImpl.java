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
import org.springframework.data.domain.Sort;
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
    @Transactional
    public TrajetDto ajouterTrajet(TrajetRequestDto dto) {
        Camion camion = camionRepository.findById(dto.getCamionId()).orElseThrow(() -> new EntityNotFoundException("Camion introuvable avec l'id : " + dto.getCamionId()));
        if (!Boolean.TRUE.equals(camion.getDisponible())) {
            throw new RuntimeException("Le camion sélectionné n'est pas disponible.");
        }
        camion.setDisponible(false);
        camionRepository.save(camion);

        Trajet trajet = trajetMapper.toEntityRequest(dto);
        trajet.setCamion(camion);
        if (trajet.getStatutTrajet()==null) {
            trajet.setStatutTrajet(StatutTrajet.PUBLIE);
        }
        Trajet trajetSaved = trajetRepository.save(trajet);
        return trajetMapper.toDto(trajetSaved);
    }

    @Override
    @Transactional
    public TrajetDto modifierTrajet(Long id, TrajetRequestDto dto) {
        Trajet trajet = trajetRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Trajet introuvable avec l'id : " + id));
        if (dto.getCamionId() != null) {
            if (trajet.getCamion() == null || !dto.getCamionId().equals(trajet.getCamion().getId())) {
                Camion ancienCamion = trajet.getCamion();
                if (ancienCamion != null) {
                    ancienCamion.setDisponible(true);
                    camionRepository.save(ancienCamion);
                }
                Camion nouveauCamion = camionRepository.findById(dto.getCamionId()).orElseThrow(() -> new EntityNotFoundException("Camion introuvable avec l'id : " + dto.getCamionId()));
                if (!Boolean.TRUE.equals(nouveauCamion.getDisponible())) {
                    throw new RuntimeException("Le camion sélectionné n'est pas disponible.");
                }
                nouveauCamion.setDisponible(false);
                camionRepository.save(nouveauCamion);
                trajet.setCamion(nouveauCamion);
            }
        }

        trajetMapper.updateEntityFromDto(dto, trajet);

        Trajet trajetUpdated = trajetRepository.save(trajet);

        return trajetMapper.toDto(trajetUpdated);
    }

    @Override
    @Transactional
    public void supprimerTrajet(Long id) {
        Trajet trajet = trajetRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Trajet introuvable avec l'id : " + id));
        Camion camion = trajet.getCamion();
        if (camion != null) {
            camion.setDisponible(true);
            camionRepository.save(camion);
        }
        trajetRepository.delete(trajet);
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
    public Page<TrajetDto> mesTrajets(String email, int page, int size) {
        User transporteur = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
        Pageable pageable = PageRequest.of(page, size);
        Page<Trajet> trajets = trajetRepository.findByCamionTransporteurId(transporteur.getId(), pageable);

        return trajets.map(trajet -> {
            TrajetDto dto = trajetMapper.toDto(trajet);
            Long nombreReservations = trajetRepository.countReservationsByTrajetId(trajet.getId());
            dto.setNombreReservations(nombreReservations.intValue());
            return dto;
        });
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

    @Override
    @Transactional(readOnly = true)
    public Page<TrajetDto> rechercherTrajets(String villeDepart, String villeArrivee, int page, int size) {

        String patternDepart = (villeDepart != null && !villeDepart.isBlank()) ? "%" + villeDepart.trim().toLowerCase() + "%" : null;
        String patternArrivee = (villeArrivee != null && !villeArrivee.isBlank()) ? "%" + villeArrivee.trim().toLowerCase() + "%" : null;

        Pageable pageable = PageRequest.of(page, size, Sort.by("dateDepart").ascending());
        Page<Trajet> trajets = trajetRepository.rechercherTrajets(patternDepart, patternArrivee, pageable);

        return trajets.map(trajetMapper::toDto);
    }
}