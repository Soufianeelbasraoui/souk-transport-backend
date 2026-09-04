package org.fleetflow.souktransportbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.request.PaiementRequestDto;
import org.fleetflow.souktransportbackend.dto.response.PaiementDto;
import org.fleetflow.souktransportbackend.entity.Cargaison;
import org.fleetflow.souktransportbackend.entity.Paiement;
import org.fleetflow.souktransportbackend.enums.MethodePaiement;
import org.fleetflow.souktransportbackend.enums.StatutPaiement;
import org.fleetflow.souktransportbackend.mapper.PaiementMapper;
import org.fleetflow.souktransportbackend.repository.CargaisonRepository;
import org.fleetflow.souktransportbackend.repository.PaiementRepository;
import org.fleetflow.souktransportbackend.service.PaiementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements PaiementService {
    private final PaiementRepository paiementRepository;
    private final CargaisonRepository cargaisonRepository;
    private final PaiementMapper paiementMapper;
    @Override
    public PaiementDto ajouterPaiement(PaiementRequestDto dto) {
        Cargaison cargaison = cargaisonRepository.findById(dto.getCargaisonId()).orElseThrow(() -> new EntityNotFoundException("Cargaison introuvable avec l'id : " + dto.getCargaisonId()));

        if (paiementRepository.existsByCargaisonId(dto.getCargaisonId())) {
            throw new IllegalArgumentException(
                    "Un paiement existe déjà pour cette cargaison."
            );
        }

        if (dto.getMontantTotal() == null || dto.getMontantTotal() <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0.");
        }

        Paiement paiement = paiementMapper.toEntity(dto);
        paiement.setCargaison(cargaison);
        paiement.setMethodePaiement(MethodePaiement.CASH);

        if (paiement.getStatutPaiement() == null) {
            paiement.setStatutPaiement(StatutPaiement.EN_ATTENTE);
        }

        return paiementMapper.toDto(paiementRepository.save(paiement));
    }

    @Override
    public PaiementDto consulterPaiement(Long id) {
        Paiement paiement = paiementRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Paiement introuvable avec l'id : " + id));
        return paiementMapper.toDto(paiement);
    }

    @Override
    public PaiementDto modifierPaiement(Long id, PaiementRequestDto dto) {
        Paiement paiement = paiementRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Paiement introuvable avec l'id : " + id));

        if (dto.getMontantTotal() != null && dto.getMontantTotal() <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0.");
        }
        paiementMapper.updateEntityFromDto(dto, paiement);
        paiement.setMethodePaiement(MethodePaiement.CASH);

        return paiementMapper.toDto(paiementRepository.save(paiement));
    }

    @Override
    public void supprimerPaiement(Long id) {
        if (!paiementRepository.existsById(id)) {
            throw new EntityNotFoundException("Paiement introuvable avec l'id : " + id);
        }
        paiementRepository.deleteById(id);
    }

    @Override
    public Page<PaiementDto> listerPaiements(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return paiementRepository.findAll(pageable).map(paiementMapper::toDto);
    }

    @Override
    public PaiementDto trouverParCargaison(Long cargaisonId) {
        Paiement paiement = paiementRepository.findByCargaisonId(cargaisonId).orElseThrow(() -> new EntityNotFoundException("Paiement introuvable pour la cargaison : " + cargaisonId));
        return paiementMapper.toDto(paiement);
    }
}