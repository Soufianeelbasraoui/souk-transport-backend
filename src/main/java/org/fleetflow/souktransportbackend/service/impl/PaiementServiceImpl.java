package org.fleetflow.souktransportbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.request.PaiementRequestDto;
import org.fleetflow.souktransportbackend.dto.response.PaiementDto;
import org.fleetflow.souktransportbackend.entity.Cargaison;
import org.fleetflow.souktransportbackend.entity.Paiement;
import org.fleetflow.souktransportbackend.entity.Reservation;
import org.fleetflow.souktransportbackend.entity.Trajet;
import org.fleetflow.souktransportbackend.enums.*;
import org.fleetflow.souktransportbackend.mapper.PaiementMapper;
import org.fleetflow.souktransportbackend.repository.CargaisonRepository;
import org.fleetflow.souktransportbackend.repository.PaiementRepository;
import org.fleetflow.souktransportbackend.repository.ReservationRepository;
import org.fleetflow.souktransportbackend.repository.TrajetRepository;
import org.fleetflow.souktransportbackend.service.PaiementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository paiementRepository;
    private final ReservationRepository reservationRepository;
    private final PaiementMapper paiementMapper;
    private final CargaisonRepository cargaisonRepository;
    private final TrajetRepository trajetRepository;

    @Override
    @Transactional
    public PaiementDto ajouterPaiement(PaiementRequestDto dto) {

        Reservation reservation = reservationRepository
                .findById(dto.getReservationId())
                .orElseThrow(() ->  new EntityNotFoundException(  "Réservation introuvable avec l'id : " + dto.getReservationId()));

        if (reservation.getStatutReservation() != StatutReservation.ACCEPTEE) {
            throw new IllegalStateException(  "Le paiement est possible uniquement pour une réservation acceptée.");
        }

        if (paiementRepository.existsByReservationId(reservation.getId())) {
            throw new IllegalArgumentException(   "Un paiement existe déjà pour cette réservation.");
        }

        if (dto.getMontantTotal() == null || dto.getMontantTotal() <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0.");
        }
        Paiement paiement = paiementMapper.toEntity(dto);
        paiement.setReservation(reservation);
        paiement.setMethodePaiement(MethodePaiement.CASH);
        paiement.setStatutPaiement(StatutPaiement.EN_ATTENTE);

        return paiementMapper.toDto(  paiementRepository.save(paiement));
    }

    @Override
    @Transactional(readOnly = true)
    public PaiementDto consulterPaiement(Long id) {
        Paiement paiement = paiementRepository.findById(id) .orElseThrow(() ->new EntityNotFoundException( "Paiement introuvable avec l'id : " + id  ));
        return paiementMapper.toDto(paiement);
    }

    @Override
    @Transactional
    public PaiementDto modifierPaiement( Long id, PaiementRequestDto dto) {

        Paiement paiement = paiementRepository.findById(id).orElseThrow(() -> new EntityNotFoundException( "Paiement introuvable avec l'id : " + id));

        if (dto.getMontantTotal() != null  && dto.getMontantTotal() <= 0) {
            throw new IllegalArgumentException( "Le montant doit être supérieur à 0.");
        }

        paiementMapper.updateEntityFromDto(dto, paiement);
        paiement.setMethodePaiement(MethodePaiement.CASH);
        return paiementMapper.toDto(    paiementRepository.save(paiement));
    }
    @Override
    @Transactional
    public PaiementDto confirmerPaiement(Long id) {

        Paiement paiement = paiementRepository.findById(id).orElseThrow(() ->new EntityNotFoundException( "Paiement introuvable avec l'id : " + id));

        if (paiement.getStatutPaiement() == StatutPaiement.PAYE) {
            throw new IllegalStateException("Ce paiement est déjà payé.");
        }
        paiement.setStatutPaiement(StatutPaiement.PAYE);
        Reservation reservation = paiement.getReservation();

        if (reservation == null) {
            throw new IllegalStateException(  "Aucune réservation associée à ce paiement.");
        }
        Cargaison cargaison = reservation.getCargaison();

        if (cargaison == null) {
            throw new IllegalStateException( "Aucune cargaison associée à cette réservation.");
        }

        if (cargaison.getStatutCargaison() != StatutCargaison.EN_TRANSIT) {
            throw new IllegalStateException(  "La cargaison doit être EN_TRANSIT avant d'être livrée.");
        }
        cargaison.setStatutCargaison(StatutCargaison.LIVREE);
        cargaisonRepository.save(cargaison);
        Trajet trajet = reservation.getTrajet();
        if (trajet == null) {
            throw new IllegalStateException(  "Aucun trajet associé à cette réservation.");
        }

        List<Cargaison> cargaisons =  cargaisonRepository.findByReservations_Trajet_Id(trajet.getId());
        boolean toutesLivrees = cargaisons.stream().allMatch(c -> c.getStatutCargaison() == StatutCargaison.LIVREE);

        if (toutesLivrees) {
            trajet.setStatutTrajet(StatutTrajet.TERMINE);
            trajetRepository.save(trajet);
        }
        paiementRepository.save(paiement);
        return paiementMapper.toDto(paiement);
    }

    @Override
    @Transactional
    public void supprimerPaiement(Long id) {
        if (!paiementRepository.existsById(id)) {
            throw new EntityNotFoundException(  "Paiement introuvable avec l'id : " + id);
        }
        paiementRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaiementDto> listerPaiements( int page,  int size) {
        Pageable pageable = PageRequest.of(page, size);
        return paiementRepository.findAll(pageable).map(paiementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PaiementDto trouverParCargaison(Long cargaisonId) {
        Paiement paiement = paiementRepository .findByReservation_Cargaison_Id(cargaisonId).orElseThrow(() -> new EntityNotFoundException("Paiement introuvable pour la cargaison : "   + cargaisonId));
        return paiementMapper.toDto(paiement);
    }
}