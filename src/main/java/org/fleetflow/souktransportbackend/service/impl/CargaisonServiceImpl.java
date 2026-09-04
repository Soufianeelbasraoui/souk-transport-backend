package org.fleetflow.souktransportbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.fleetflow.souktransportbackend.dto.request.CargaisonRequestDto;
import org.fleetflow.souktransportbackend.dto.response.CargaisonDto;
import org.fleetflow.souktransportbackend.entity.Cargaison;
import org.fleetflow.souktransportbackend.entity.Expediteur;
import org.fleetflow.souktransportbackend.entity.Trajet;
import org.fleetflow.souktransportbackend.mapper.CargaisonMapper;
import org.fleetflow.souktransportbackend.repository.CargaisonRepository;
import org.fleetflow.souktransportbackend.repository.ExpediteurRepository;
import org.fleetflow.souktransportbackend.repository.TrajetRepository;
import org.fleetflow.souktransportbackend.service.CargaisonService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CargaisonServiceImpl implements CargaisonService {
    private final CargaisonRepository cargaisonRepository;
    private final ExpediteurRepository expediteurRepository;
    private final TrajetRepository trajetRepository;
    private final CargaisonMapper cargaisonMapper;
    @Override
    public CargaisonDto ajouterCargaison(CargaisonRequestDto dto) {
        if (dto.getPoids() == null || dto.getPoids() <= 0) {
            throw new IllegalArgumentException("Le poids doit être supérieur à 0");
        }
        Expediteur expediteur = expediteurRepository.findById(dto.getExpediteurId()).orElseThrow(() -> new EntityNotFoundException("Expéditeur introuvable avec l'id : " + dto.getExpediteurId()));
        Trajet trajet = null;
        if (dto.getTrajetId() != null) {
            trajet = trajetRepository.findById(dto.getTrajetId()).orElseThrow(() -> new EntityNotFoundException("Trajet introuvable avec l'id : " + dto.getTrajetId()));
        }
        Cargaison cargaison = cargaisonMapper.toEntity(dto);
        cargaison.setExpediteur(expediteur);
        cargaison.setTrajet(trajet);
        return cargaisonMapper.toDto(cargaisonRepository.save(cargaison));
    }

    @Override
    public CargaisonDto modifierCargaison(Long id, CargaisonRequestDto dto) {
        Cargaison cargaison = cargaisonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cargaison introuvable avec l'id : " + id));
        if (dto.getPoids() != null && dto.getPoids() <= 0) {
            throw new IllegalArgumentException("Le poids doit être supérieur à 0");
        }

        if (dto.getExpediteurId() != null) {
            Expediteur expediteur = expediteurRepository.findById(dto.getExpediteurId()).orElseThrow(() -> new EntityNotFoundException("Expéditeur introuvable avec l'id : " + dto.getExpediteurId()));
            cargaison.setExpediteur(expediteur);
        }

        if (dto.getTrajetId() != null) {
            Trajet trajet = trajetRepository.findById(dto.getTrajetId()).orElseThrow(() -> new EntityNotFoundException("Trajet introuvable avec l'id : " + dto.getTrajetId()));
            cargaison.setTrajet(trajet);
        }
        cargaisonMapper.updateEntityFromDto(dto, cargaison);
        return cargaisonMapper.toDto(cargaisonRepository.save(cargaison));
    }

    @Override
    public void supprimerCargaison(Long id) {
        Cargaison cargaison = cargaisonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cargaison introuvable avec l'id : " + id));
        cargaisonRepository.delete(cargaison);
    }

    @Override
    public CargaisonDto consulterCargaison(Long id) {
        Cargaison cargaison = cargaisonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cargaison introuvable avec l'id : " + id));
        return cargaisonMapper.toDto(cargaison);
    }

    @Override
    public List<CargaisonDto> listerCargaisons() {
        return cargaisonMapper.toDtoList(cargaisonRepository.findAll());
    }

    @Override
    public List<CargaisonDto> listerCargaisonsExpediteur(Long expediteurId) {
        return cargaisonMapper.toDtoList(cargaisonRepository.findByExpediteurId(expediteurId));
    }

    @Override
    public Page<CargaisonDto> listerCargaisons(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cargaisonRepository.findAll(pageable).map(cargaisonMapper::toDto);
    }

    @Override
    public List<CargaisonDto> listerParTrajet(Long trajetId) {
        return cargaisonMapper.toDtoList(cargaisonRepository.findByTrajetId(trajetId));
    }

    @Override
    public Page<CargaisonDto> rechercher(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cargaisonRepository.findByDescriptionContainingIgnoreCase(keyword, pageable)
                .map(cargaisonMapper::toDto);
    }
}