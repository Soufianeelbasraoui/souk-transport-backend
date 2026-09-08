package org.fleetflow.souktransportbackend.repository;

import org.fleetflow.souktransportbackend.entity.Transporteur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransporteurRepository extends JpaRepository<Transporteur, Long> {
    Page<Transporteur> findAll(Pageable pageable);
    Optional<Transporteur> findByEmail(String email);
}
