package org.fleetflow.souktransportbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.request.TrajetRequestDto;
import org.fleetflow.souktransportbackend.dto.response.TrajetDto;
import org.fleetflow.souktransportbackend.service.TrajetService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trajets")
@RequiredArgsConstructor
public class TrajetController {

    private final TrajetService trajetService;

    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR')")
    @PostMapping
    @Operation(summary = "Ajouter un nouveau trajet")
    public ResponseEntity<TrajetDto> ajouterTrajet(@Valid @RequestBody TrajetRequestDto dto) {
        TrajetDto nouveauTrajet = trajetService.ajouterTrajet(dto);
        return new ResponseEntity<>(nouveauTrajet, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR')")
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un trajet existant par son ID")
    public ResponseEntity<TrajetDto> modifierTrajet(@PathVariable Long id, @Valid @RequestBody TrajetRequestDto dto) {
        TrajetDto trajetModifie = trajetService.modifierTrajet(id, dto);
        return ResponseEntity.ok(trajetModifie);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un trajet par son ID")
    public ResponseEntity<Void> supprimerTrajet(@PathVariable Long id) {
        trajetService.supprimerTrajet(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR', 'EXPEDITEUR')")
    @GetMapping("/{id}")
    @Operation(summary = "Consulter les détails d'un trajet par son ID")
    public ResponseEntity<TrajetDto> consulterTrajet(@PathVariable Long id) {
        TrajetDto trajet = trajetService.consulterTrajet(id);
        return ResponseEntity.ok(trajet);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR', 'EXPEDITEUR')")
    @GetMapping
    @Operation(summary = "Lister les trajets avec pagination")
    public ResponseEntity<Page<TrajetDto>> listerTrajets(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<TrajetDto> trajets = trajetService.listerTrajets(page, size);
        return ResponseEntity.ok(trajets);
    }

    @GetMapping("/publies")
    @Operation(summary = "Lister tous les trajets publiés")
    public ResponseEntity<List<TrajetDto>> listerTrajetsPublies() {
        List<TrajetDto> trajetsPublies = trajetService.listerTrajetsPublies();
        return ResponseEntity.ok(trajetsPublies);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR', 'EXPEDITEUR')")
    @GetMapping("/trier")
    @Operation(summary = "Lister et trier les trajets avec pagination")
    public ResponseEntity<Page<TrajetDto>> trierTrajets(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction) {
        Page<TrajetDto> trajetsTries = trajetService.trierTrajets(page, size, sortBy, direction);
        return ResponseEntity.ok(trajetsTries);
    }
    @GetMapping("/recent")
    public Page<TrajetDto> getRecentTrajets(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        return trajetService.RecentTrajets(page, size);
    }
}