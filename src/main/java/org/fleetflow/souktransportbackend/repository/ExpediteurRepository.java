package org.fleetflow.souktransportbackend.repository;

import org.fleetflow.souktransportbackend.entity.Expediteur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpediteurRepository extends JpaRepository<Expediteur, Long> {
    Page<Expediteur> findAll(Pageable pageable);
}
