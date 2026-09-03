package org.fleetflow.souktransportbackend.repository;

import org.fleetflow.souktransportbackend.entity.Cargaison;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CargaisonRepository extends JpaRepository<Cargaison, Long> {
    List<Cargaison> findByExpediteurId(Long expediteurId);
    List<Cargaison> findByTrajetId(Long trajetId);
    Page<Cargaison> findByDescriptionContainingIgnoreCase(String keyword, Pageable pageable);
}