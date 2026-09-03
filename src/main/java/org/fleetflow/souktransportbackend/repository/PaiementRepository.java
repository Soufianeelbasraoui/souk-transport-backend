package org.fleetflow.souktransportbackend.repository;

import org.fleetflow.souktransportbackend.entity.Paiement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    Page<Paiement> findAll(Pageable pageable);
    Optional<Paiement> findByCargaisonId(Long cargaisonId);
    boolean existsByCargaisonId(Long cargaisonId);
}