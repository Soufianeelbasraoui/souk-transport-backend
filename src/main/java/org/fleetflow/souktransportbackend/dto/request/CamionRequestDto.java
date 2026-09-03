package org.fleetflow.souktransportbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fleetflow.souktransportbackend.enums.TypeCamion;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CamionRequestDto {

    @NotBlank(message = "La marque est obligatoire")
    private String marque;

    @NotBlank(message = "Le modèle est obligatoire")
    private String modele;

    @NotNull(message = "Le type de camion est obligatoire")
    private TypeCamion type;

    @NotBlank(message = "L'immatriculation est obligatoire")
    private String immatriculation;

    @NotNull(message = "La capacité est obligatoire")
    private Double capacite;

    @NotNull(message = "Le transporteur est obligatoire")
    private Long transporteurId;

    private Boolean disponible;
}