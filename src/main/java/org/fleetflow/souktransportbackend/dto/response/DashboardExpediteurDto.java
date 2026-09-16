package org.fleetflow.souktransportbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardExpediteurDto {

    private Long totalCargaisons;
    private Long totalReservations;
    private Long annulee;
    private Long refusee;
    private Long acceptee;
}