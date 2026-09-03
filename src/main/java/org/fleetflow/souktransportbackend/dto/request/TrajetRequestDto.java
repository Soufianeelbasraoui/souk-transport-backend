package org.fleetflow.souktransportbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fleetflow.souktransportbackend.enums.StatutTrajet;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrajetRequestDto {
    private String villeDepart;
    private String villeArrivee;
    private LocalDateTime dateDepart;
    private Double prix;
    private double poidsDisponible;
    private StatutTrajet statutTrajet;
    private Long camionId;
}
