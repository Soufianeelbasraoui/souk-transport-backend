package org.fleetflow.souktransportbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.request.UserRequestDto;
import org.fleetflow.souktransportbackend.dto.response.UserDto;
import org.fleetflow.souktransportbackend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDto> ajouterUser(@Valid @RequestBody UserRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.ajouterUser(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> trouverUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.trouverUser(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserDto>> listerUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.listerUsers(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> modifierUser(@PathVariable Long id,@Valid @RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(userService.modifierUser(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerUser(@PathVariable Long id) {
        userService.supprimerUser(id);
        return ResponseEntity.noContent().build();
    }
}