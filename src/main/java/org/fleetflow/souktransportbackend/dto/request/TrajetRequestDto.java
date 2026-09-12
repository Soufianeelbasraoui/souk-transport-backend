package org.fleetflow.souktransportbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrajetRequestDto {

    @NotBlank(message = "La ville de départ est obligatoire")
    private String villeDepart;

    @NotBlank(message = "La ville d'arrivée est obligatoire")
    private String villeArrivee;

    @NotNull(message = "La date de départ est obligatoire")
    private LocalDateTime dateDepart;

    @NotNull(message = "Le prix est obligatoire")
    private Double prix;

    @NotNull(message = "Le poids disponible est obligatoire")
    private Double poidsDisponible;

    @NotNull(message = "Le camion est obligatoire")
    private Long camionId;
}