package org.fleetflow.souktransportbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.fleetflow.souktransportbackend.dto.request.CargaisonRequestDto;
import org.fleetflow.souktransportbackend.dto.response.CargaisonDto;
import org.fleetflow.souktransportbackend.entity.Cargaison;
import org.fleetflow.souktransportbackend.entity.Expediteur;
import org.fleetflow.souktransportbackend.entity.User;
import org.fleetflow.souktransportbackend.enums.Role;
import org.fleetflow.souktransportbackend.enums.StatutCargaison;
import org.fleetflow.souktransportbackend.enums.StatutReservation;
import org.fleetflow.souktransportbackend.mapper.CargaisonMapper;
import org.fleetflow.souktransportbackend.repository.CargaisonRepository;
import org.fleetflow.souktransportbackend.repository.ExpediteurRepository;
import org.fleetflow.souktransportbackend.repository.UserRepository;
import org.fleetflow.souktransportbackend.service.CargaisonService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CargaisonServiceImpl implements CargaisonService {
    private final CargaisonRepository cargaisonRepository;
    private final ExpediteurRepository expediteurRepository;
    private final CargaisonMapper cargaisonMapper;
    private final UserRepository userRepository;
    @Override
    public CargaisonDto ajouterCargaison(CargaisonRequestDto dto, String email) {
        if (dto.getPoids() == null || dto.getPoids() <= 0) {
            throw new IllegalArgumentException("Le poids doit être supérieur à 0");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        Expediteur expediteur;

        if (user.getRole() == Role.ADMIN) {
            if (dto.getExpediteurId() == null) {
                throw new IllegalArgumentException("L'id de l'expéditeur est obligatoire pour un ADMIN");
            }
            expediteur = expediteurRepository.findById(dto.getExpediteurId()).orElseThrow(() -> new EntityNotFoundException("Expéditeur introuvable avec l'id : " + dto.getExpediteurId()));
        } else {
            expediteur = expediteurRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Expéditeur introuvable pour cet utilisateur"));
        }

        Cargaison cargaison = cargaisonMapper.toEntity(dto);
        cargaison.setExpediteur(expediteur);

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

        cargaisonMapper.updateEntityFromDto(dto, cargaison);
        return cargaisonMapper.toDto(cargaisonRepository.save(cargaison));
    }

    @Override
    public void supprimerCargaison(Long id) {
        Cargaison cargaison = cargaisonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cargaison introuvable avec l'id : " + id));
        cargaisonRepository.delete(cargaison);
    }

    @Override
    @Transactional(readOnly = true)
    public CargaisonDto consulterCargaison(Long id) {
        Cargaison cargaison = cargaisonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cargaison introuvable avec l'id : " + id));
        return cargaisonMapper.toDto(cargaison);
    }


    @Override
    public List<CargaisonDto> listerCargaisonsExpediteur(Long expediteurId) {
        return cargaisonMapper.toDtoList(cargaisonRepository.findByExpediteurId(expediteurId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CargaisonDto> listerCargaisons(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cargaisonRepository.findAll(pageable).map(cargaisonMapper::toDto);
    }

    @Override
    public List<CargaisonDto> listerParTrajet(Long trajetId) {
        return cargaisonMapper.toDtoList(cargaisonRepository.findByReservations_Trajet_Id(trajetId));
    }

//    @Override
//    @Cacheable(value = "cargaisonsSearch", key = "#keyword + ':page:' + #page + ':size:' + #size")
//    public Page<CargaisonDto> rechercher(String keyword, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return cargaisonRepository.findByDescriptionContainingIgnoreCase(keyword, pageable).map(cargaisonMapper::toDto);
//    }

    @Override
    @Transactional(readOnly = true)
    public List<CargaisonDto> mesCargaisonsDisponibles(String email){
        User user=userRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("Utilisateur introuvable"));

        return cargaisonMapper.toDtoList(cargaisonRepository.findCargaisonsDisponibles(user.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CargaisonDto>  mesCargaisons(String email){
        User user=userRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("expediteur introvable"));
        return cargaisonMapper.toDtoList(cargaisonRepository.findCargaisonsByExpediteurId(user.getId()));
    }


}