package org.fleetflow.souktransportbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.request.CamionRequestDto;
import org.fleetflow.souktransportbackend.dto.response.CamionDto;
import org.fleetflow.souktransportbackend.entity.Camion;
import org.fleetflow.souktransportbackend.entity.Transporteur;
import org.fleetflow.souktransportbackend.entity.User;
import org.fleetflow.souktransportbackend.enums.TypeCamion;
import org.fleetflow.souktransportbackend.mapper.CamionMapper;
import org.fleetflow.souktransportbackend.repository.CamionRepository;
import org.fleetflow.souktransportbackend.repository.TransporteurRepository;
import org.fleetflow.souktransportbackend.repository.UserRepository;
import org.fleetflow.souktransportbackend.service.CamionService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CamionServiceImpl implements CamionService {

    private final CamionRepository camionRepository;
    private final TransporteurRepository transporteurRepository;
    private final CamionMapper camionMapper;
    private final UserRepository userRepository;
    @Override
    @CacheEvict(value = {"camion","camions","camionsByTransporteur","camionsByTransporteurPage","camionsByType","camionsByCapacite","camionsPage","camionsSearch","camionsSorted"}, allEntries = true)
    public CamionDto ajouterCamion(CamionRequestDto dto, String emailUserConnecte) {
        if (dto.getImmatriculation() == null || dto.getImmatriculation().isBlank()) {
            throw new IllegalArgumentException("L'immatriculation est obligatoire");
        }
        if (dto.getCapacite() == null || dto.getCapacite() <= 0) {
            throw new IllegalArgumentException("La capacité doit être supérieure à 0");
        }

        String immatriculation = dto.getImmatriculation().trim().toUpperCase();
        if (camionRepository.existsByImmatriculation(immatriculation)) {
            throw new IllegalArgumentException("L'immatriculation existe déjà : " + immatriculation);
        }
        Transporteur transporteur = transporteurRepository.findByEmail(emailUserConnecte).orElseThrow(() -> new EntityNotFoundException("Transporteur introuvable avec l'email : " + emailUserConnecte));

        Camion camion = camionMapper.toEntityRequest(dto);
        camion.setTransporteur(transporteur);
        camion.setImmatriculation(immatriculation);

        if (camion.getDisponible() == null) {
            camion.setDisponible(true);
        }
        return camionMapper.toDto(camionRepository.save(camion));
    }

    @Override
    @CacheEvict(value = {"camion","camions","camionsByTransporteur","camionsByTransporteurPage","camionsByType","camionsByCapacite","camionsPage","camionsSearch","camionsSorted"}, allEntries = true)
    public CamionDto modifierCamion(Long id, CamionRequestDto dto) {
        Camion camion = camionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Camion introuvable avec l'id : " + id));
        if (dto.getImmatriculation() != null) {
            String immatriculation = dto.getImmatriculation().trim().toUpperCase();
            if (camionRepository.existsByImmatriculationAndIdNot(immatriculation, id)) {
                throw new IllegalArgumentException("L'immatriculation existe déjà : " + immatriculation);
            }
            dto.setImmatriculation(immatriculation);
        }
        if (dto.getCapacite() != null && dto.getCapacite() <= 0) {
            throw new IllegalArgumentException("La capacité doit être supérieure à 0");
        }
        camionMapper.updateEntityFromDto(dto, camion);

        return camionMapper.toDto(camionRepository.save(camion));
    }
    @Override
    @CacheEvict(value = {"camion","camions","camionsByTransporteur","camionsByTransporteurPage","camionsByType","camionsByCapacite","camionsPage","camionsSearch","camionsSorted"}, allEntries = true)
    public void supprimerCamion(Long id) {
        Camion camion = camionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Camion introuvable avec l'id : " + id));
        camionRepository.delete(camion);
    }

    @Override
    @Cacheable(value = "camion", key = "#id")
    public CamionDto consulterCamion(Long id) {
        Camion camion = camionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Camion introuvable avec l'id : " + id));
        return camionMapper.toDto(camion);
    }

    @Override
    @Cacheable(value = "camionsByTransporteur", key = "#transporteurId")
    public List<CamionDto> listerCamionsTransporteur(Long transporteurId) {
        return camionMapper.toDtoList(camionRepository.findByTransporteurId(transporteurId));
    }

    @Override
    @Cacheable(value = "camionsByTransporteurPage", key = "'tid:' + #transporteurId + ':page:' + #page + ':size:' + #size")
    public Page<CamionDto> listerCamionsTransporteur(Long transporteurId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return camionRepository.findByTransporteurId(transporteurId, pageable).map(camionMapper::toDto);
    }

    @Override
    @Cacheable(value = "camionsByType", key = "#transporteurId + ':' + #type")
    public List<CamionDto> listerParType(Long transporteurId, String type) {
        TypeCamion typeCamion;
        try {
            typeCamion = TypeCamion.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type de camion invalide : " + type);
        }
        return camionMapper.toDtoList(camionRepository.findByTransporteurIdAndType(transporteurId, typeCamion));
    }

    @Override
    @Cacheable(value = "camionsByCapacite", key = "#transporteurId + ':' + #capacite")
    public List<CamionDto> listerParCapaciteSuperieure(Long transporteurId, Double capacite) {
        return camionMapper.toDtoList(camionRepository.findByTransporteurIdAndCapaciteGreaterThanEqual(transporteurId, capacite));
    }

    @Override
    @Cacheable(value = "camionsSearch", key = "#transporteurId + ':' + #keyword + ':page:' + #page + ':size:' + #size")
    public Page<CamionDto> rechercher(Long transporteurId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return camionRepository.findByTransporteurIdAndImmatriculationContainingIgnoreCase(transporteurId, keyword, pageable).map(camionMapper::toDto);
    }

    @Override
    @Cacheable(value = "camionsSorted", key = "#transporteurId + ':page:' + #page + ':size:' + #size + ':sort:' + #sortBy + ':dir:' + #direction")
    public Page<CamionDto> trierCamions(Long transporteurId, int page, int size, String sortBy, String direction) {
        List<String> champsAutorises = List.of("id", "marque", "modele", "capacite", "immatriculation");
        if (!champsAutorises.contains(sortBy)) {
            throw new IllegalArgumentException("Champ de tri invalide : " + sortBy);
        }
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return camionRepository.findByTransporteurId(transporteurId, pageable).map(camionMapper::toDto);
    }

    @Override
    @Cacheable(value = "mesCamions", key = "#email")
    public List<CamionDto> mesCamions(String email) {
        User transporteur = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
        List<Camion> camions = camionRepository.findByTransporteurId(transporteur.getId());
        return camionMapper.toDtoList(camions);
    }
}