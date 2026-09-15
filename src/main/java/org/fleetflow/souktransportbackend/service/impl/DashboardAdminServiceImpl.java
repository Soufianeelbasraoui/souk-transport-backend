package org.fleetflow.souktransportbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.response.DashboardAdminDto;
import org.fleetflow.souktransportbackend.dto.response.ReservationDto;
import org.fleetflow.souktransportbackend.dto.response.TrajetDto;
import org.fleetflow.souktransportbackend.entity.Reservation;
import org.fleetflow.souktransportbackend.entity.Trajet;
import org.fleetflow.souktransportbackend.enums.StatutPaiement;
import org.fleetflow.souktransportbackend.enums.StatutTrajet;
import org.fleetflow.souktransportbackend.mapper.ReservationMapper;
import org.fleetflow.souktransportbackend.mapper.TrajetMapper;
import org.fleetflow.souktransportbackend.repository.*;
import org.fleetflow.souktransportbackend.service.DashboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardAdminServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final TrajetRepository trajetRepository;
    private final ReservationRepository reservationRepository;
    private final CargaisonRepository cargaisonRepository;
    private final PaiementRepository paiementRepository;
    private final TrajetMapper trajetMapper;
    private final ReservationMapper reservationMapper;

    @Override
    @Transactional(readOnly = true)
    public DashboardAdminDto getDashboardAdmin() {

        DashboardAdminDto dto = new DashboardAdminDto();

        dto.setTotalUtilisateurs(userRepository.count());
        dto.setTrajetsPublies(  trajetRepository.countByStatutTrajet(StatutTrajet.PUBLIE));
        dto.setTotalReservations(reservationRepository.count());
        dto.setTotalCargaisons(cargaisonRepository.count());
        dto.setRevenusTotaux(paiementRepository.calculerRevenusTotal(StatutPaiement.PAYE));

        Page<Trajet> trajets = trajetRepository.findAllByOrderByIdDesc(PageRequest.of(0, 5));
        List<TrajetDto> derniersTrajets = new ArrayList<>();
        for (Trajet trajet : trajets) {
            derniersTrajets.add( trajetMapper.toDto(trajet) );
        }
        dto.setDerniersTrajets(derniersTrajets);

        List<Reservation> reservations = reservationRepository.findFirst5ByOrderByIdDesc();
        List<ReservationDto> reservationsRecentes = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationsRecentes.add(reservationMapper.toDto(reservation));
        }
        dto.setReservationsRecentes(reservationsRecentes);

        return dto;
    }
}