package org.fleetflow.souktransportbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.request.ReservationRequestDto;
import org.fleetflow.souktransportbackend.dto.response.ReservationDto;
import org.fleetflow.souktransportbackend.entity.*;
import org.fleetflow.souktransportbackend.enums.StatutCargaison;
import org.fleetflow.souktransportbackend.enums.StatutReservation;
import org.fleetflow.souktransportbackend.mapper.ReservationMapper;
import org.fleetflow.souktransportbackend.repository.*;
import org.fleetflow.souktransportbackend.service.ReservationService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TrajetRepository trajetRepository;
    private final CargaisonRepository cargaisonRepository;
    private final ReservationMapper reservationMapper;
    private final ExpediteurRepository expediteurRepository;
    private final TransporteurRepository transporteurRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReservationDto ajouterReservation(ReservationRequestDto dto) {
        Trajet trajet = trajetRepository.findById(dto.getTrajetId()).orElseThrow(() -> new EntityNotFoundException("Trajet introuvable : " + dto.getTrajetId()));

        Cargaison cargaison = cargaisonRepository.findById(dto.getCargaisonId()).orElseThrow(() -> new EntityNotFoundException("Cargaison introuvable : " + dto.getCargaisonId()));

        if (cargaison.getPoids() <= 0)
            throw new IllegalStateException("Poids de cargaison invalide.");

        if (cargaison.getStatutCargaison() == StatutCargaison.EN_TRANSIT)
            throw new IllegalStateException("Cargaison déjà en transit.");

        if (trajet.getPoidsDisponible() < cargaison.getPoids())
            throw new IllegalStateException("Poids disponible insuffisant.");

        boolean existe = reservationRepository.findByCargaisonId(cargaison.getId())
                .stream()
                .anyMatch(r -> r.getStatutReservation() == StatutReservation.EN_ATTENTE || r.getStatutReservation() == StatutReservation.ACCEPTEE);

        if (existe) throw new IllegalStateException("Cette cargaison possède déjà une réservation.");

        Reservation reservation = reservationMapper.toEntity(dto);
        reservation.setTrajet(trajet);
        reservation.setCargaison(cargaison);
        reservation.setPoidsReserve(cargaison.getPoids());
        reservation.setStatutReservation(StatutReservation.EN_ATTENTE);

        reservation.setPrixConvenu(dto.getPrixConvenu() != null ? dto.getPrixConvenu() : trajet.getPrix());

        return reservationMapper.toDto(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationDto modifierReservation(Long id, ReservationRequestDto dto) {

        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Réservation introuvable : " + id));

        if (reservation.getStatutReservation() != StatutReservation.EN_ATTENTE)
            throw new IllegalStateException("Seules les réservations en attente peuvent être modifiées.");

        Trajet trajet = reservation.getTrajet();
        Cargaison cargaison = reservation.getCargaison();

        if (dto.getTrajetId() != null)
            trajet = trajetRepository.findById(dto.getTrajetId()).orElseThrow(() -> new EntityNotFoundException("Trajet introuvable : " + dto.getTrajetId()));

        if (dto.getCargaisonId() != null)
            cargaison = cargaisonRepository.findById(dto.getCargaisonId()).orElseThrow(() -> new EntityNotFoundException("Cargaison introuvable : " + dto.getCargaisonId()));

        if (cargaison.getPoids() <= 0){
            throw new IllegalStateException("Poids de cargaison invalide.");
        }

        if (cargaison.getStatutCargaison() == StatutCargaison.EN_TRANSIT){
            throw new IllegalStateException("Cargaison déjà en transit.");
        }

        if (trajet.getPoidsDisponible() < cargaison.getPoids()){
            throw new IllegalStateException("Poids disponible insuffisant.");
        }

        reservation.setTrajet(trajet);
        reservation.setCargaison(cargaison);
        reservation.setPoidsReserve(cargaison.getPoids());

        reservation.setPrixConvenu(dto.getPrixConvenu() != null ? dto.getPrixConvenu() : trajet.getPrix()
        );
        return reservationMapper.toDto(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public void supprimerReservation(Long id) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Réservation introuvable : " + id));

        reservationRepository.delete(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationDto consulterReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Réservation introuvable : " + id));
        return reservationMapper.toDto(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> listerReservations() {
        return reservationRepository.findAll().stream().map(reservationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDto> listerReservations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reservationRepository.findAll(pageable).map(reservationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> listerParTrajet(Long trajetId) {
        return reservationRepository.findByTrajetId(trajetId).stream().map(reservationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> listerParCargaison(Long cargaisonId) {
        return reservationRepository.findByCargaisonId(cargaisonId).stream().map(reservationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countReservationsExpediteur(String email) {

        User expediteur = expediteurRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Expéditeur introuvable."));
        return reservationRepository.countByCargaisonExpediteurId(expediteur.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Long countReservationsTransporteur(String email) {

        User transporteur = transporteurRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Transporteur introuvable."));

        return reservationRepository.countByTrajetCamionTransporteurId(transporteur.getId());
    }

    @Override
    @Transactional
    public ReservationDto accepterReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new EntityNotFoundException("Réservation introuvable : " + reservationId));

        if (reservation.getStatutReservation() != StatutReservation.EN_ATTENTE)
            throw new IllegalStateException("Cette réservation ne peut plus être acceptée.");

        Trajet trajet = reservation.getTrajet();
        Cargaison cargaison = reservation.getCargaison();
        Double poids = reservation.getPoidsReserve();

        if (poids == null || poids <= 0){
            throw new IllegalStateException("Poids réservé invalide.");
        }


        if (trajet.getPoidsDisponible() < poids){
            throw new IllegalStateException("Poids disponible insuffisant.");
        }

        List<Reservation> autres = reservationRepository.findByCargaisonId(cargaison.getId()).stream().filter(r -> !r.getId().equals(reservationId) && r.getStatutReservation() == StatutReservation.EN_ATTENTE).toList();
        autres.forEach(r -> r.setStatutReservation(StatutReservation.REFUSEE));
        reservationRepository.saveAll(autres);

        trajet.setPoidsDisponible(trajet.getPoidsDisponible() - poids);
        cargaison.setStatutCargaison(StatutCargaison.EN_TRANSIT);
        reservation.setStatutReservation(StatutReservation.ACCEPTEE);

        trajetRepository.save(trajet);
        cargaisonRepository.save(cargaison);

        return reservationMapper.toDto(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationDto refuserReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new EntityNotFoundException("Réservation introuvable : " + reservationId));

        if (reservation.getStatutReservation() != StatutReservation.EN_ATTENTE)
            throw new IllegalStateException("Cette réservation ne peut plus être refusée.");
        reservation.setStatutReservation(StatutReservation.REFUSEE);

        return reservationMapper.toDto(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationDto annulerReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new EntityNotFoundException("Réservation introuvable : " + reservationId));

        if (reservation.getStatutReservation() == StatutReservation.ANNULEE){
            throw new IllegalStateException("Réservation déjà annulée.");
        }

        if (reservation.getStatutReservation() == StatutReservation.ACCEPTEE) {

            Trajet trajet = reservation.getTrajet();
            trajet.setPoidsDisponible(trajet.getPoidsDisponible() + reservation.getPoidsReserve());

            Cargaison cargaison = reservation.getCargaison();
            cargaison.setStatutCargaison(StatutCargaison.SOUMISE);

            trajetRepository.save(trajet);
            cargaisonRepository.save(cargaison);
        }

        reservation.setStatutReservation(StatutReservation.ANNULEE);

        return reservationMapper.toDto(reservationRepository.save(reservation));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> mesReservationTransporteur(String email) {

        User transporteur = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable."));
        return reservationRepository.findByTrajet_Camion_TransporteurId(transporteur.getId()).stream().map(reservationMapper::toDto).toList();
    }
}