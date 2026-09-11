package org.fleetflow.souktransportbackend.repository;

import org.fleetflow.souktransportbackend.dto.response.ReservationDto;
import org.fleetflow.souktransportbackend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByCargaison_Id(Long cargaisonId);

    List<Reservation> findByTrajetId(Long trajetId);
    List<Reservation> findByCargaisonId(Long cargaisonId);

    long countByTrajetCamionTransporteurId(Long transporteurId);
    long countByCargaisonExpediteurId(Long expediteurId);

    List<Reservation> findByTrajet_Camion_TransporteurId(Long transporteurId);
}
