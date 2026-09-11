package org.fleetflow.souktransportbackend.repository;

import org.fleetflow.souktransportbackend.entity.Cargaison;
import org.fleetflow.souktransportbackend.entity.User;
import org.fleetflow.souktransportbackend.enums.StatutCargaison;
import org.fleetflow.souktransportbackend.enums.StatutReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CargaisonRepository extends JpaRepository<Cargaison, Long> {
    List<Cargaison> findByExpediteurId(Long expediteurId);
    List<Cargaison> findByReservations_Trajet_Id(Long trajetId);
    Long countByExpediteurId(Long expediteurId);
}