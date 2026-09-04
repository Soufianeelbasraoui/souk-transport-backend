package org.fleetflow.souktransportbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.fleetflow.souktransportbackend.dto.request.CargaisonRequestDto;
import org.fleetflow.souktransportbackend.dto.response.CargaisonDto;
import org.fleetflow.souktransportbackend.service.CargaisonService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cargaisons")
@RequiredArgsConstructor
public class CargaisonController {
    private final CargaisonService cargaisonService;
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR')")
    @PostMapping
    public ResponseEntity<CargaisonDto> ajouterCargaison(@Valid @RequestBody CargaisonRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cargaisonService.ajouterCargaison(dto));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR')")
    @PutMapping("/{id}")
    public ResponseEntity<CargaisonDto> modifierCargaison(@PathVariable Long id,@Valid @RequestBody CargaisonRequestDto dto) {
        return ResponseEntity.ok(cargaisonService.modifierCargaison(id, dto));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCargaison(@PathVariable Long id) {
        cargaisonService.supprimerCargaison(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    @GetMapping("/{id}")
    public ResponseEntity<CargaisonDto> consulterCargaison(@PathVariable Long id) {
        return ResponseEntity.ok(cargaisonService.consulterCargaison(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    @GetMapping
    public ResponseEntity<List<CargaisonDto>> listerCargaisons() {
        return ResponseEntity.ok(cargaisonService.listerCargaisons());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR')")
    @GetMapping("/expediteur/{expediteurId}")
    public ResponseEntity<List<CargaisonDto>> listerCargaisonsExpediteur(@PathVariable Long expediteurId) {
        return ResponseEntity.ok(cargaisonService.listerCargaisonsExpediteur(expediteurId));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")    @GetMapping("/page")
    public ResponseEntity<Page<CargaisonDto>> listerCargaisons(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(cargaisonService.listerCargaisons(page, size));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    @GetMapping("/trajet/{trajetId}")
    public ResponseEntity<List<CargaisonDto>> listerParTrajet(@PathVariable Long trajetId) {
        return ResponseEntity.ok(cargaisonService.listerParTrajet(trajetId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    @GetMapping("/recherche")
    public ResponseEntity<Page<CargaisonDto>> rechercher(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(cargaisonService.rechercher(keyword, page, size));
    }
}