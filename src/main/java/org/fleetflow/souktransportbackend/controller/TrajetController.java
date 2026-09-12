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
import org.springframework.security.core.Authentication;
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
        return ResponseEntity.ok(trajetService.modifierTrajet(id, dto));
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
        return ResponseEntity.ok(trajetService.consulterTrajet(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR', 'EXPEDITEUR')")
    @GetMapping
    @Operation(summary = "Lister les trajets avec pagination")
    public ResponseEntity<Page<TrajetDto>> listerTrajets(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(trajetService.listerTrajets(page, size));
    }

    @GetMapping("/publies")
    @Operation(summary = "Lister tous les trajets publiés")
    public ResponseEntity<List<TrajetDto>> listerTrajetsPublies() {
        List<TrajetDto> trajetsPublies = trajetService.listerTrajetsPublies();
        return ResponseEntity.ok(trajetsPublies);
    }

    @GetMapping("/recent")
    public Page<TrajetDto> getRecentTrajets(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        return trajetService.recentTrajets(page, size);
    }
    @GetMapping("/mesTrajets")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR')")
    public ResponseEntity<List<TrajetDto>> getMesTrajets(Authentication authentication) {
        return ResponseEntity.ok(trajetService.mesTrajets(authentication.getName()));
    }

    @GetMapping("/countTrajet")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR')")
    public ResponseEntity<Long> getCountTrajet(Authentication authentication){
        return ResponseEntity.ok(trajetService.countTrajet(authentication.getName()));
    }
}