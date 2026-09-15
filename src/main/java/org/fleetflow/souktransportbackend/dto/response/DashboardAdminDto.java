package org.fleetflow.souktransportbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardAdminDto {
    private Long totalUtilisateurs;
    private Long trajetsPublies;
    private Long totalCargaisons;
    private Long totalReservations;
    private Double revenusTotaux;
    private List<TrajetDto> derniersTrajets;
    private List<ReservationDto> reservationsRecentes;
}