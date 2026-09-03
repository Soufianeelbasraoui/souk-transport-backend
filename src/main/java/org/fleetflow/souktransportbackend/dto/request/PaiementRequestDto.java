package org.fleetflow.souktransportbackend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fleetflow.souktransportbackend.enums.MethodePaiement;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementRequestDto {

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être supérieur à 0")
    private Double montantTotal;

    @NotNull(message = "La méthode de paiement est obligatoire")
    private MethodePaiement methodePaiement;

    @NotNull(message = "La cargaison est obligatoire")
    private Long cargaisonId;
}