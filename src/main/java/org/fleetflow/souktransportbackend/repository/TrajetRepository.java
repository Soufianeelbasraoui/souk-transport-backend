package org.fleetflow.souktransportbackend.repository;
import org.fleetflow.souktransportbackend.entity.Trajet;
import org.fleetflow.souktransportbackend.enums.StatutTrajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {

    List<Trajet> findByStatutTrajet(StatutTrajet statutTrajet);
    Page<Trajet> findByStatutTrajetOrderByDateDepartDesc(StatutTrajet statutTrajet, Pageable pageable);
    List<Trajet> findByCamionTransporteurId(Long transporteurId);
    Long countByCamionTransporteurId(Long id);
}