package org.fleetflow.souktransportbackend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {

    @NotNull(message = "La cargaison est obligatoire")
    private Long cargaisonId;

    @NotNull(message = "Le trajet est obligatoire")
    private Long trajetId;
    private Double prixConvenu;
}