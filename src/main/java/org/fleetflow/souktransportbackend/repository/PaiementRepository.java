package org.fleetflow.souktransportbackend.repository;

import org.fleetflow.souktransportbackend.entity.Paiement;
import org.fleetflow.souktransportbackend.enums.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    Optional<Paiement> findByReservation_Cargaison_Id( Long cargaisonId);
    boolean existsByReservationId(Long reservationId);
//    @Query("SELECT COALESCE(SUM(p.montantTotal), 0) FROM Paiement p WHERE p.statutPaiement = :statut\n")
//    Double calculerRevenusTotal(  @Param("statut") StatutPaiement statut  );
     @Query("SELECT SUM(p.montantTotal) FROM Paiement p WHERE p.statutPaiement = :statut")
     Double calculerRevenusTotal(@Param("statut") StatutPaiement statut);

    @Query(" SELECT SUM(p.montantTotal) FROM Paiement p WHERE p.reservation.trajet.camion.transporteur.id = :transporteurId AND p.statutPaiement = 'PAYE'")
    Double countRevenuByTransporteurId(@Param("transporteurId") Long transporteurId);
}