package org.fleetflow.souktransportbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.request.PaiementRequestDto;
import org.fleetflow.souktransportbackend.dto.response.PaiementDto;
import org.fleetflow.souktransportbackend.service.PaiementService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementService paiementService;

    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR')")
    @PostMapping
    @Operation(summary = "Enregistrer un nouveau paiement")
    public ResponseEntity<PaiementDto> ajouterPaiement(@Valid @RequestBody PaiementRequestDto dto) {
        PaiementDto paiement = paiementService.ajouterPaiement(dto);
        return new ResponseEntity<>(paiement, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    @GetMapping("/{id}")
    @Operation(summary = "Consulter un paiement")
    public ResponseEntity<PaiementDto> consulterPaiement(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.consulterPaiement(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR')")
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un paiement")
    public ResponseEntity<PaiementDto> modifierPaiement(@PathVariable Long id, @Valid @RequestBody PaiementRequestDto dto) {
        return ResponseEntity.ok(paiementService.modifierPaiement(id, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR')")
    @PatchMapping("/{id}/payer")
    @Operation(summary = "Confirmer le paiement")
    public ResponseEntity<PaiementDto> confirmerPaiement( @PathVariable Long id) {
        return ResponseEntity.ok( paiementService.confirmerPaiement(id) );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un paiement")
    public ResponseEntity<Void> supprimerPaiement( @PathVariable Long id) {
        paiementService.supprimerPaiement(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    @GetMapping
    @Operation(summary = "Lister les paiements")
    public ResponseEntity<Page<PaiementDto>> listerPaiements( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(paiementService.listerPaiements(page, size));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    @GetMapping("/cargaison/{cargaisonId}")
    @Operation(summary = "Trouver le paiement d'une cargaison")
    public ResponseEntity<PaiementDto> trouverParCargaison( @PathVariable Long cargaisonId) {
        return ResponseEntity.ok( paiementService.trouverParCargaison(cargaisonId));
    }
}