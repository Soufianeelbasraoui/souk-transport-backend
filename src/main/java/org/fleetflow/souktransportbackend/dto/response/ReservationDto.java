package org.fleetflow.souktransportbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fleetflow.souktransportbackend.enums.StatutCargaison;
import org.fleetflow.souktransportbackend.enums.StatutReservation;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private Long id;
    private LocalDateTime dateReservation;
    private Double poidsReserve;
    private Double prixConvenu;
    private StatutReservation statutReservation;

    private Long trajetId;
    private String villeDepart;
    private String villeArrivee;

    private Long cargaisonId;
    private String description;
    private Double poids;
    private StatutCargaison statutCargaison;

    private Long expediteurId;
    private String expediteurNom;
    private String expediteurPrenom;
}
