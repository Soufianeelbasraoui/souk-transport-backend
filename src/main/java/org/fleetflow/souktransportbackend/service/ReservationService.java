package org.fleetflow.souktransportbackend.service;

import org.fleetflow.souktransportbackend.dto.request.ReservationRequestDto;
import org.fleetflow.souktransportbackend.dto.response.ReservationDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReservationService {
    ReservationDto ajouterReservation(ReservationRequestDto dto);
    ReservationDto modifierReservation(Long id,ReservationRequestDto dto);
    void supprimerReservation(Long id);
    ReservationDto consulterReservation(Long id);
    List<ReservationDto> listerReservations();
    Page<ReservationDto> listerReservations(int page,int size);
    List<ReservationDto> listerParTrajet(Long trajetId);
    List<ReservationDto> listerParCargaison(Long cargaisonId);
   Long countReservationsExpediteur(String email);
   Long countReservationsTransporteur(String email);

    ReservationDto accepterReservation(Long reservationId);
    ReservationDto refuserReservation(Long reservationId);
    ReservationDto annulerReservation(Long reservationId);

    List<ReservationDto> mesReservationTransporteur(String email);
}
