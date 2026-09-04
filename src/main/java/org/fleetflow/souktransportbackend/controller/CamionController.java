package org.fleetflow.souktransportbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.fleetflow.souktransportbackend.dto.request.CamionRequestDto;
import org.fleetflow.souktransportbackend.dto.response.CamionDto;
import org.fleetflow.souktransportbackend.service.CamionService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/camions")
@RequiredArgsConstructor
public class CamionController {
    private final CamionService camionService;

    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR')")
    @PostMapping
    public ResponseEntity<CamionDto> ajouterCamion( @Valid @RequestBody CamionRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(camionService.ajouterCamion(dto));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR')")
    @PutMapping("/{id}")
    public ResponseEntity<CamionDto> modifierCamion( @PathVariable Long id,@Valid @RequestBody CamionRequestDto dto) {
        return ResponseEntity.ok(camionService.modifierCamion(id, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCamion(@PathVariable Long id) {
        camionService.supprimerCamion(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR', 'EXPEDITEUR')")
    @GetMapping("/{id}")
    public ResponseEntity<CamionDto> consulterCamion(@PathVariable Long id) {
        return ResponseEntity.ok(camionService.consulterCamion(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR', 'EXPEDITEUR')")
    @GetMapping("/transporteur/{transporteurId}")
    public ResponseEntity<List<CamionDto>> listerCamionsTransporteur(@PathVariable Long transporteurId) {
        return ResponseEntity.ok(camionService.listerCamionsTransporteur(transporteurId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR', 'EXPEDITEUR')")
    @GetMapping("/transporteur/{transporteurId}/page")
    public ResponseEntity<Page<CamionDto>> listerCamionsTransporteur(@PathVariable Long transporteurId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(camionService.listerCamionsTransporteur(transporteurId, page, size));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR', 'EXPEDITEUR')")
    @GetMapping("/transporteur/{transporteurId}/type")
    public ResponseEntity<List<CamionDto>> listerParType(@PathVariable Long transporteurId, @RequestParam String type) {
        return ResponseEntity.ok(camionService.listerParType(transporteurId, type));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR', 'EXPEDITEUR')")
    @GetMapping("/transporteur/{transporteurId}/capacite")
    public ResponseEntity<List<CamionDto>> listerParCapaciteSuperieure(@PathVariable Long transporteurId, @RequestParam Double capacite) {
        return ResponseEntity.ok(camionService.listerParCapaciteSuperieure(transporteurId, capacite));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR')")
    @GetMapping("/transporteur/{transporteurId}/recherche")
    public ResponseEntity<Page<CamionDto>> rechercher(@PathVariable Long transporteurId, @RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(camionService.rechercher(transporteurId, keyword, page, size));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'TRANSPORTEUR')")
    @GetMapping("/transporteur/{transporteurId}/tri")
    public ResponseEntity<Page<CamionDto>> trierCamions(@PathVariable Long transporteurId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String direction) {
        return ResponseEntity.ok(camionService.trierCamions(transporteurId, page, size, sortBy, direction));
    }
}
