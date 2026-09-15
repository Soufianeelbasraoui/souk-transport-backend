package org.fleetflow.souktransportbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DashboardTransporteurDto {
    private String nom;
    private Long trajetsPublies;
    private Long nombreCamions;
    private Double revenus;
    private Long reservationsRecues;
}
