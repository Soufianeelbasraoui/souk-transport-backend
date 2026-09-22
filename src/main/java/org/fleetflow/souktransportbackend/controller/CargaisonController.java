package org.fleetflow.souktransportbackend.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.fleetflow.souktransportbackend.dto.request.CargaisonRequestDto;
import org.fleetflow.souktransportbackend.dto.response.CargaisonDto;
import org.fleetflow.souktransportbackend.entity.User;
import org.fleetflow.souktransportbackend.enums.StatutCargaison;
import org.fleetflow.souktransportbackend.repository.UserRepository;
import org.fleetflow.souktransportbackend.service.CargaisonService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cargaisons")
@RequiredArgsConstructor
public class CargaisonController {
    private final CargaisonService cargaisonService;
    private final UserRepository userRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR')")
    @PostMapping
    public ResponseEntity<CargaisonDto> ajouterCargaison(@Valid @RequestBody CargaisonRequestDto dto,Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cargaisonService.ajouterCargaison(dto,authentication.getName()));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR')")
    @PutMapping("/{id}")
    public ResponseEntity<CargaisonDto> modifierCargaison(@PathVariable Long id,@Valid @RequestBody CargaisonRequestDto dto) {
        return ResponseEntity.ok(cargaisonService.modifierCargaison(id, dto));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR')")
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

    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR')")
    @GetMapping("/expediteur/{expediteurId}")
    public ResponseEntity<List<CargaisonDto>> listerCargaisonsExpediteur(@PathVariable Long expediteurId) {
        return ResponseEntity.ok(cargaisonService.listerCargaisonsExpediteur(expediteurId));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
    @GetMapping("/lister")
    public ResponseEntity<Page<CargaisonDto>> listerCargaisons(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(cargaisonService.listerCargaisons(page, size));
    }
//
//    @PreAuthorize("hasAnyRole('ADMIN', 'EXPEDITEUR', 'TRANSPORTEUR')")
//    @GetMapping("/trajet/{trajetId}")
//    public ResponseEntity<List<CargaisonDto>> listerParTrajet(@PathVariable Long trajetId) {
//        return ResponseEntity.ok(cargaisonService.listerParTrajet(trajetId));
//    }


    @PreAuthorize("hasRole('EXPEDITEUR')")
    @GetMapping("/mes-cargaisons-disponibles")
    public ResponseEntity<List<CargaisonDto>> mesCargaisonsDisponibles(Authentication authentication){
         return ResponseEntity.ok(cargaisonService.mesCargaisonsDisponibles(authentication.getName()));
    }

@GetMapping("/mes-cargaisons")
@PreAuthorize("hasRole('EXPEDITEUR')")
public ResponseEntity<Page<CargaisonDto>> mesCargaisons(
        Authentication authentication,
        @RequestParam(required = false) StatutCargaison statut,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "9") int size
) {
    return ResponseEntity.ok( cargaisonService.mesCargaisons(authentication.getName(), statut,page, size ));
}
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CargaisonDto>> rechercherParDescription(
            @RequestParam String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(cargaisonService.rechercherParDescription(description, page, size ));
    }

    @GetMapping("/filter/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CargaisonDto>> filtrerParStatut(
            @RequestParam StatutCargaison statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok( cargaisonService.filtrerParStatut(statut, page, size ));
    }

}