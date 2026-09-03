package org.fleetflow.souktransportbackend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargaisonRequestDto {

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "Le poids est obligatoire")
    private Double poids;

    @NotNull(message = "Le trajet est obligatoire")
    private Long trajetId;

    @NotNull(message = "L'expéditeur est obligatoire")
    private Long expediteurId;
}