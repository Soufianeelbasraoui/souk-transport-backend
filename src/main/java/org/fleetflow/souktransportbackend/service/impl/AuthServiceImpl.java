package org.fleetflow.souktransportbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.request.ExpediteurRequestDto;
import org.fleetflow.souktransportbackend.dto.request.LoginRequestDto;
import org.fleetflow.souktransportbackend.dto.request.RegisterRequest;
import org.fleetflow.souktransportbackend.dto.request.TransporteurRequestDto;
import org.fleetflow.souktransportbackend.dto.response.AuthResponseDto;
import org.fleetflow.souktransportbackend.entity.Admin;
import org.fleetflow.souktransportbackend.entity.Expediteur;
import org.fleetflow.souktransportbackend.entity.Transporteur;
import org.fleetflow.souktransportbackend.entity.User;
import org.fleetflow.souktransportbackend.enums.Role;
import org.fleetflow.souktransportbackend.enums.StatutUser;
import org.fleetflow.souktransportbackend.repository.UserRepository;
import org.fleetflow.souktransportbackend.security.JwtUtil;
import org.fleetflow.souktransportbackend.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponseDto registerExpediteur(ExpediteurRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        Expediteur expediteur = new Expediteur();
        expediteur.setNom(request.getNom());
        expediteur.setPrenom(request.getPrenom());
        expediteur.setEmail(request.getEmail());
        expediteur.setPassword(passwordEncoder.encode(request.getPassword()));
        expediteur.setTelephone(request.getTelephone());
        expediteur.setVille(request.getVille());
        expediteur.setRole(Role.EXPEDITEUR);
        expediteur.setStatutUser(StatutUser.ACTIF);
        expediteur.setNomEntreprise(request.getNomEntreprise());
        expediteur.setAdresseEntreprise(request.getAdresseEntreprise());

        Expediteur savedExpediteur = userRepository.save(expediteur);
        String token = jwtUtil.generateToken(savedExpediteur.getEmail(), savedExpediteur.getNom(), savedExpediteur.getRole().name());
        return new AuthResponseDto(token);
    }

    @Override
    @Transactional
    public AuthResponseDto registerTransporteur(TransporteurRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        Transporteur transporteur = new Transporteur();
        transporteur.setNom(request.getNom());
        transporteur.setPrenom(request.getPrenom());
        transporteur.setEmail(request.getEmail());
        transporteur.setPassword(passwordEncoder.encode(request.getPassword()));
        transporteur.setTelephone(request.getTelephone());
        transporteur.setVille(request.getVille());
        transporteur.setRole(Role.TRANSPORTEUR);
        transporteur.setStatutUser(StatutUser.ACTIF);
        transporteur.setCin(request.getCin());
        transporteur.setNumeroPermis(request.getNumeroPermis());

        Transporteur savedTransporteur = userRepository.save(transporteur);
        String token = jwtUtil.generateToken(savedTransporteur.getEmail(), savedTransporteur.getNom(), savedTransporteur.getRole().name());
        return new AuthResponseDto(token);
    }
    @Override
    public AuthResponseDto register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Admin admin = new Admin();
        admin.setNom(request.getNom());
        admin.setPrenom(request.getPrenom());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setTelephone(request.getTelephone());
        admin.setVille(request.getVille());
        admin.setRole(Role.ADMIN);
        admin.setStatutUser(StatutUser.ACTIF);
        Admin savedAdmin = userRepository.save(admin);

        String token = jwtUtil.generateToken(
                savedAdmin.getEmail(),
                savedAdmin.getNom(),
                savedAdmin.getRole().name()
        );

        return new AuthResponseDto(token);
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        String token = jwtUtil.generateToken(user.getEmail(), user.getNom(), user.getRole().name());
        return new AuthResponseDto(token);
    }
}