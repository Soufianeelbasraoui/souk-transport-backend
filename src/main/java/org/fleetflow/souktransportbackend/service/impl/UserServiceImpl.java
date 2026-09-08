package org.fleetflow.souktransportbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fleetflow.souktransportbackend.dto.request.UserRequestDto;
import org.fleetflow.souktransportbackend.dto.response.UserDto;
import org.fleetflow.souktransportbackend.entity.User;
import org.fleetflow.souktransportbackend.mapper.UserMapper;
import org.fleetflow.souktransportbackend.repository.UserRepository;
import org.fleetflow.souktransportbackend.service.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @CacheEvict(value = "usersById", allEntries = true)
    public UserDto ajouterUser(UserRequestDto dto) {
        User user = userMapper.toEntityRequest(dto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @CacheEvict(value = "usersById", key = "#id")
    public UserDto modifierUser(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'id : " + id));
        userMapper.updateEntityFromDto(dto, user);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Cacheable(value = "usersById", key = "#id")
    public UserDto trouverUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'id : " + id));
        return userMapper.toDto(user);
    }

    @Override
    @CacheEvict(value = "usersById", key = "#id")
    public void supprimerUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Utilisateur introuvable avec l'id : " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "users", key = "'page:' + #page + ':size:' + #size")
    public Page<UserDto> listerUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }
}