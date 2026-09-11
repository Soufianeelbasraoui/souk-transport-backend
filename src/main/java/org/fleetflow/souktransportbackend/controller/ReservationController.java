package org.fleetflow.souktransportbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.request.ReservationRequestDto;
import org.fleetflow.souktransportbackend.dto.response.ReservationDto;
import org.fleetflow.souktransportbackend.service.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR')")
    public ResponseEntity<ReservationDto> createReservation(@Valid @RequestBody ReservationRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.ajouterReservation(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    public ResponseEntity<ReservationDto> modifierReservation(@PathVariable Long id, @Valid @RequestBody ReservationRequestDto dto) {
        return ResponseEntity.ok(reservationService.modifierReservation(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    public ResponseEntity<Void> supprimerReservation(@PathVariable Long id) {
        reservationService.supprimerReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/accepter")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR')")
    public ResponseEntity<ReservationDto> accepterReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.accepterReservation(id));
    }

    @PatchMapping("/{id}/refuser")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR')")
    public ResponseEntity<ReservationDto> refuserReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.refuserReservation(id));
    }

    @PatchMapping("/{id}/annuler")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR')")
    public ResponseEntity<ReservationDto> annulerReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.annulerReservation(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    public ResponseEntity<ReservationDto> consulterReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.consulterReservation(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    public ResponseEntity<List<ReservationDto>> listerReservations() {
        return ResponseEntity.ok(reservationService.listerReservations());
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    public ResponseEntity<Page<ReservationDto>> listerReservations(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reservationService.listerReservations(page, size));
    }

    @GetMapping("/trajet/{trajetId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    public ResponseEntity<List<ReservationDto>> listerParTrajet(@PathVariable Long trajetId) {
        return ResponseEntity.ok(reservationService.listerParTrajet(trajetId));
    }

    @GetMapping("/cargaison/{cargaisonId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    public ResponseEntity<List<ReservationDto>> listerParCargaison(@PathVariable Long cargaisonId) {
        return ResponseEntity.ok(reservationService.listerParCargaison(cargaisonId));
    }


    @GetMapping("/transporteur/count")
    @PreAuthorize("hasRole('TRANSPORTEUR')")
    public ResponseEntity<Long> countMesReservationsTransporteur(Authentication authentication) {
        return ResponseEntity.ok(reservationService.countReservationsTransporteur(authentication.getName()));
    }

    @GetMapping("/expediteur/count")
    @PreAuthorize("hasRole('EXPEDITEUR')")
    public ResponseEntity<Long> countMesReservationsExpediteur(Authentication authentication) {
        return ResponseEntity.ok(reservationService.countReservationsExpediteur(authentication.getName()));
    }

    @GetMapping("/transporteur/mes-reservations")
    @PreAuthorize("hasRole('TRANSPORTEUR')")
    public ResponseEntity<List<ReservationDto>> getMesReservationTransporteur(Authentication authentication){
        return ResponseEntity.ok(reservationService.mesReservationTransporteur(authentication.getName()));
    }
}