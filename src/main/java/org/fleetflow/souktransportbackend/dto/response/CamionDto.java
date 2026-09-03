package org.fleetflow.souktransportbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fleetflow.souktransportbackend.enums.TypeCamion;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CamionDto {

    private Long id;

    private String marque;

    private String modele;

    private TypeCamion type;

    private String immatriculation;

    private Double capacite;

    private Boolean disponible;

    private Long transporteurId;
}