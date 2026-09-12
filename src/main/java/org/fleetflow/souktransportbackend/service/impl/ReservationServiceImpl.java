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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private  final ReservationRepository reservationRepository;
    private final TrajetRepository trajetRepository;
    private final CargaisonRepository cargaisonRepository;
    private final ReservationMapper reservationMapper;
    private final ExpediteurRepository expediteurRepository;
    private final TransporteurRepository transporteurRepository;
    private final UserRepository userRepository;

    @Override
    public ReservationDto ajouterReservation(ReservationRequestDto dto) {
       Trajet trajet=trajetRepository.findById(dto.getTrajetId()).orElseThrow(()->new EntityNotFoundException("Trajet introuvalble avec l'id :"+ dto.getTrajetId()));
       Cargaison cargaison=cargaisonRepository.findById(dto.getCargaisonId()).orElseThrow(()->new EntityNotFoundException("Cargaison introuvalble avec le id :"+dto.getCargaisonId()));
       if (cargaison.getPoids()==null || cargaison.getPoids()<=0){
           throw new IllegalStateException("Le poids de la cargaison est invalide.");
       }
       if(cargaison.getStatutCargaison()==StatutCargaison.EN_TRANSIT){
           throw new IllegalStateException("Cette cargaison est déja en transit.");
       }
       if (trajet.getPoidsDisponible()<cargaison.getPoids()){
           throw new IllegalStateException("Le poids de la cargaison dépasse le poids disponible du trajet.");
       }
       Boolean  reservationExiste=reservationRepository.findByCargaisonId(cargaison.getId()).stream().anyMatch(r->r.getStatutReservation()==StatutReservation.EN_ATTENTE || r.getStatutReservation()==StatutReservation.ACCEPTEE);
       if (reservationExiste){
           throw new IllegalStateException("Cette cargaison possède déjà une réservation.");
       }
       Reservation reservation=reservationMapper.toEntity(dto);
       reservation.setTrajet(trajet);
       reservation.setCargaison(cargaison);
       reservation.setPoidsReserve(cargaison.getPoids());
       reservation.setStatutReservation(StatutReservation.EN_ATTENTE);

       if (dto.getPrixConvenu()==null){
           reservation.setPrixConvenu(trajet.getPrix()!=null ?trajet.getPrix():0.0);
       }else {
           reservation.setPrixConvenu(dto.getPrixConvenu());
       }
       return reservationMapper.toDto(reservationRepository.save(reservation));
    }

    @Override
    public ReservationDto modifierReservation(Long id,ReservationRequestDto dto){
       Reservation reservation=reservationRepository.findById(id).orElseThrow(()->new EntityNotFoundException( "Réservation introuvable avec l'id : " + id));

       if (dto.getTrajetId() !=null){
           Trajet trajet =trajetRepository.findById(dto.getTrajetId()).orElseThrow(()->new EntityNotFoundException("Trajet introuvable avec l'id :"+dto.getTrajetId()));
           reservation.setTrajet(trajet);
       }
       if (dto.getCargaisonId()!=null){
           Cargaison cargaison=cargaisonRepository.findById(dto.getCargaisonId()).orElseThrow(()->new EntityNotFoundException("Cargaison introuvable avec l'id :"+dto.getCargaisonId()));
           reservation.setCargaison(cargaison);
       }
       reservationMapper.updateEntityFromDto(dto,reservation);
       return reservationMapper.toDto(reservationRepository.save(reservation));
    }
    @Override
    public void supprimerReservation(Long id){
       Reservation reservation=reservationRepository.findById(id).orElseThrow(()->new EntityNotFoundException( "Réservation introuvable avec l'id : " + id));
       reservationRepository.delete(reservation);
    }

    @Override
    public ReservationDto consulterReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Réservation introuvable avec l'id : " + id));
        return reservationMapper.toDto(reservation);
    }

    @Override
    public List<ReservationDto> listerReservations(){
       return reservationRepository.findAll().stream().map(reservationMapper::toDto).toList();
    }

    @Override
    public Page<ReservationDto> listerReservations(int page ,int size){
        Pageable pageable= PageRequest.of(page,size);
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
   public Long countReservationsExpediteur(String email){
      User expediteur=expediteurRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("Expéditeur introuvable"));
       return reservationRepository.countByCargaisonExpediteurId(expediteur.getId());
   }

   @Override
    public  Long countReservationsTransporteur(String email){
       User transporteur=transporteurRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("Transportuer introuvable"));
       return reservationRepository.countByTrajetCamionTransporteurId(transporteur.getId());
   }


    @Override
    @Transactional
    public ReservationDto accepterReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new EntityNotFoundException("Réservation introuvable avec l'id : " + reservationId));

        if (reservation.getStatutReservation() != StatutReservation.EN_ATTENTE) {
            throw new IllegalStateException("Cette réservation ne peut plus être acceptée.");
        }

        Trajet trajet = reservation.getTrajet();
        Cargaison cargaison = reservation.getCargaison();

        Double poidsReserve = reservation.getPoidsReserve();

        if (poidsReserve == null || poidsReserve <= 0) {
            throw new IllegalStateException("Le poids réservé est invalide.");
        }

        if (trajet.getPoidsDisponible() < poidsReserve) {
            throw new IllegalStateException("Le poids disponible dans le trajet n'est plus suffisant.");
        }

        List<Reservation> autresReservations =
                reservationRepository.findByCargaisonId(cargaison.getId())
                        .stream()
                        .filter(r -> !r.getId().equals(reservationId) && r.getStatutReservation() == StatutReservation.EN_ATTENTE).toList();

        for (Reservation autre : autresReservations) {
            autre.setStatutReservation(StatutReservation.REFUSEE);
        }

        reservationRepository.saveAll(autresReservations);

        trajet.setPoidsDisponible(trajet.getPoidsDisponible() - poidsReserve);

        reservation.setStatutReservation(StatutReservation.ACCEPTEE);

        cargaison.setStatutCargaison(StatutCargaison.EN_TRANSIT);

        trajetRepository.save(trajet);
        cargaisonRepository.save(cargaison);

        return reservationMapper.toDto(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationDto refuserReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new EntityNotFoundException("Réservation introuvable avec l'id : " + reservationId));
        if (reservation.getStatutReservation() != StatutReservation.EN_ATTENTE) {
            throw new IllegalStateException("Cette réservation ne peut plus être refusée.");
        }

        reservation.setStatutReservation(StatutReservation.REFUSEE);
        return reservationMapper.toDto(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationDto annulerReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new EntityNotFoundException("Réservation introuvable avec l'id : " + reservationId));

        if (reservation.getStatutReservation() == StatutReservation.ANNULEE) {
            throw new IllegalStateException("Cette réservation est déjà annulée.");
        }

        if (reservation.getStatutReservation() == StatutReservation.ACCEPTEE) {
            Trajet trajet = reservation.getTrajet();
            trajet.setPoidsDisponible(trajet.getPoidsDisponible() + reservation.getPoidsReserve());
            trajetRepository.save(trajet);
            Cargaison cargaison = reservation.getCargaison();
            cargaison.setStatutCargaison(StatutCargaison.SOUMISE);
            cargaisonRepository.save(cargaison);
        }

        reservation.setStatutReservation(StatutReservation.ANNULEE);

        return reservationMapper.toDto(reservationRepository.save(reservation));
    }

    @Override
    public List<ReservationDto> mesReservationTransporteur(String email) {
        User transporteur=userRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("Utilisateur introuvable") );
        return reservationRepository.findByTrajet_Camion_TransporteurId(transporteur.getId()).stream().map(reservationMapper::toDto).toList();
    }


}
