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
    @Operation(summary = "Enregistrer un nouveau paiement pour une cargaison")
    public ResponseEntity<PaiementDto> ajouterPaiement(@Valid @RequestBody PaiementRequestDto dto) {
        PaiementDto nouveauPaiement = paiementService.ajouterPaiement(dto);
        return new ResponseEntity<>(nouveauPaiement, HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    @GetMapping("/{id}")
    @Operation(summary = "Consulter les détails d'un paiement par son ID")
    public ResponseEntity<PaiementDto> consulterPaiement(@PathVariable Long id) {
        PaiementDto paiement = paiementService.consulterPaiement(id);
        return ResponseEntity.ok(paiement);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR')")
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un paiement existant par son ID")
    public ResponseEntity<PaiementDto> modifierPaiement(@PathVariable Long id, @Valid @RequestBody PaiementRequestDto dto) {
        PaiementDto paiementModifie = paiementService.modifierPaiement(id, dto);
        return ResponseEntity.ok(paiementModifie);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un paiement par son ID")
    public ResponseEntity<Void> supprimerPaiement(@PathVariable Long id) {
        paiementService.supprimerPaiement(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    @GetMapping
    @Operation(summary = "Lister tous les paiements avec pagination")
    public ResponseEntity<Page<PaiementDto>> listerPaiements(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<PaiementDto> paiements = paiementService.listerPaiements(page, size);
        return ResponseEntity.ok(paiements);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    @GetMapping("/cargaison/{cargaisonId}")
    @Operation(summary = "Trouver le paiement associé à une cargaison")
    public ResponseEntity<PaiementDto> trouverParCargaison(@PathVariable Long cargaisonId) {
        PaiementDto paiement = paiementService.trouverParCargaison(cargaisonId);
        return ResponseEntity.ok(paiement);
    }
}