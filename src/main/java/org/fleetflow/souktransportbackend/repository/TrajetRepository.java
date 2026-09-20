package org.fleetflow.souktransportbackend.repository;
import org.fleetflow.souktransportbackend.entity.Trajet;
import org.fleetflow.souktransportbackend.enums.StatutTrajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {

    List<Trajet> findByStatutTrajet(StatutTrajet statutTrajet);
    Page<Trajet> findByStatutTrajetOrderByDateDepartDesc(StatutTrajet statutTrajet, Pageable pageable);
    List<Trajet> findByCamionTransporteurId(Long transporteurId);
    Page<Trajet> findByCamionTransporteurId(Long transporteurId, Pageable pageable);
    Long countByCamionTransporteurId(Long id);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.trajet.id = :trajetId")
    Long countReservationsByTrajetId(@Param("trajetId") Long trajetId);
    Long countByStatutTrajet(StatutTrajet statutTrajet);

    Page<Trajet> findAllByOrderByIdDesc(Pageable pageable);
    Page<Trajet> findByVilleDepartContainingIgnoreCaseOrVilleArriveeContainingIgnoreCase(String villeDepart, String villeArrivee, Pageable pageable);

    @Query("SELECT t FROM Trajet t WHERE " +
           "(:patternDepart IS NULL OR LOWER(t.villeDepart) LIKE :patternDepart) AND " +
           "(:patternArrivee IS NULL OR LOWER(t.villeArrivee) LIKE :patternArrivee)")
    Page<Trajet> rechercherTrajets(
            @Param("patternDepart") String patternDepart,
            @Param("patternArrivee") String patternArrivee,
            Pageable pageable);

}