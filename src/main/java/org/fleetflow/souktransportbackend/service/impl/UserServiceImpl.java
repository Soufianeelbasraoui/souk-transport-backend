package org.fleetflow.souktransportbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.request.UserRequestDto;
import org.fleetflow.souktransportbackend.dto.response.UserDto;
import org.fleetflow.souktransportbackend.entity.User;
import org.fleetflow.souktransportbackend.enums.Role;
import org.fleetflow.souktransportbackend.enums.StatutUser;
import org.fleetflow.souktransportbackend.mapper.UserMapper;
import org.fleetflow.souktransportbackend.repository.UserRepository;
import org.fleetflow.souktransportbackend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto ajouterUser(UserRequestDto dto) {
        User user = userMapper.toEntityRequest(dto);
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto modifierUser(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'id : " + id));
        String currentPassword = user.getPassword();
        userMapper.updateEntityFromDto(dto, user);
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            user.setPassword(currentPassword);
        }
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto trouverUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'id : " + id));
        return userMapper.toDto(user);
    }

    @Override
    public void supprimerUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Utilisateur introuvable avec l'id : " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public Page<UserDto> listerUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    @Override
    public Page<UserDto> rechercherParNom(String nom, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByNomContainingIgnoreCase(nom, pageable).map(userMapper::toDto);
    }

    @Override
    public Page<UserDto> filtrerParStatut(StatutUser statut, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return userRepository.findByStatutUser(statut, pageable).map(userMapper::toDto);
    }

    @Override
    public Page<UserDto> filtrerParRole(Role role, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByRole(role, pageable).map(userMapper::toDto);
    }
}