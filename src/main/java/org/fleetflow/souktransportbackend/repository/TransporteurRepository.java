package org.fleetflow.souktransportbackend.repository;

import org.fleetflow.souktransportbackend.entity.Transporteur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransporteurRepository extends JpaRepository<Transporteur, Long> {
    Page<Transporteur> findAll(Pageable pageable);
}
