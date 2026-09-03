package org.fleetflow.souktransportbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fleetflow.souktransportbackend.enums.MethodePaiement;
import org.fleetflow.souktransportbackend.enums.StatutPaiement;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementDto {
    private Long id;
    private Double montantTotal;
    private StatutPaiement statutPaiement;
    private MethodePaiement methodePaiement;
    private Long cargaisonId;
}
