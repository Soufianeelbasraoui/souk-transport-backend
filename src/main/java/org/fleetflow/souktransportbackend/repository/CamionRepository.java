package org.fleetflow.souktransportbackend.repository;

import org.fleetflow.souktransportbackend.entity.Camion;
import org.fleetflow.souktransportbackend.enums.TypeCamion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CamionRepository extends JpaRepository<Camion, Long> {
    boolean existsByImmatriculation(String immatriculation);
    boolean existsByImmatriculationAndIdNot(String immatriculation, Long id);
    List<Camion> findByDisponibleTrue();
    List<Camion> findByCapaciteGreaterThan(Double capacite);
    List<Camion> findByTransporteurId(Long transporteurId);
    Page<Camion> findByTransporteurId(Long transporteurId, Pageable pageable);
    List<Camion> findByTransporteurIdAndDisponible(Long transporteurId, Boolean disponible);
    List<Camion> findByTransporteurIdAndType(Long transporteurId, TypeCamion type);
    List<Camion> findByTransporteurIdAndCapaciteGreaterThanEqual(Long transporteurId, Double capacite);
    Page<Camion> findByTransporteurIdAndImmatriculationContainingIgnoreCase(Long transporteurId, String immatriculation, Pageable pageable);
    Page<Camion> findByTransporteurIdAndMarqueContainingIgnoreCase(Long transporteurId, String marque, Pageable pageable);
}