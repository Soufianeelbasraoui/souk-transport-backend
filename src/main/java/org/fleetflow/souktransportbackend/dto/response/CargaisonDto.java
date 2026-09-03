package org.fleetflow.souktransportbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fleetflow.souktransportbackend.enums.StatutCargaison;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargaisonDto {
    private Long id;
    private String description;
    private Double poids;
    private StatutCargaison statutCargaison;
    private Long trajetId;
    private Long expediteurId;
}
