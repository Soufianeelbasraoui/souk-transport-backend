package org.fleetflow.souktransportbackend.service.impl;


import org.fleetflow.souktransportbackend.dto.request.CamionRequestDto;
import org.fleetflow.souktransportbackend.dto.response.CamionDto;
import org.fleetflow.souktransportbackend.entity.Camion;
import org.fleetflow.souktransportbackend.entity.Transporteur;
import org.fleetflow.souktransportbackend.entity.User;
import org.fleetflow.souktransportbackend.enums.Role;
import org.fleetflow.souktransportbackend.mapper.CamionMapper;
import org.fleetflow.souktransportbackend.repository.CamionRepository;
import org.fleetflow.souktransportbackend.repository.TransporteurRepository;
import org.fleetflow.souktransportbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CamionServiceImplTest {

    @Mock
    private CamionRepository camionRepository;

    @Mock
    private TransporteurRepository transporteurRepository;

    @Mock
    private CamionMapper camionMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CamionServiceImpl camionService;

    @Test
    void ajouterCamion_shouldAddCamionSuccessfully() {

        String email = "omar@gmail.com";

        CamionRequestDto request = new CamionRequestDto();
        request.setImmatriculation("abc-123");
        request.setCapacite(20.0);
        request.setTransporteurId(1L);

        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setRole(Role.TRANSPORTEUR);

        Transporteur transporteur = new Transporteur();
        transporteur.setId(1L);

        Camion camion = new Camion();
        camion.setImmatriculation("ABC-123");
        camion.setCapacite(20.0);

        CamionDto camionDto = new CamionDto();

        when(camionRepository.existsByImmatriculation("ABC-123")).thenReturn(false);
        when(userRepository.findByEmail(email)) .thenReturn(java.util.Optional.of(user));
        when(transporteurRepository.findById(1L)).thenReturn(java.util.Optional.of(transporteur));
        when(camionMapper.toEntityRequest(request)).thenReturn(camion);
        when(camionRepository.save(camion)) .thenReturn(camion);
        when(camionMapper.toDto(camion)).thenReturn(camionDto);

        CamionDto result =camionService.ajouterCamion(request, email);

        assertNotNull(result);
        assertEquals("ABC-123", camion.getImmatriculation());
        assertTrue(camion.getDisponible());
        verify(camionRepository).existsByImmatriculation("ABC-123");
        verify(userRepository).findByEmail(email);
        verify(camionRepository).save(camion);

        verify(camionMapper).toDto(camion);
    }

    @Test
    void modifierCamion_shouldUpdateCamionSuccessfully() {

        Long camionId = 1L;

        CamionRequestDto request = new CamionRequestDto();
        request.setImmatriculation("DEF-456");
        request.setCapacite(30.0);

        Camion camion = new Camion();
        camion.setId(camionId);
        camion.setImmatriculation("ABC-123");
        camion.setCapacite(20.0);

        CamionDto camionDto = new CamionDto();


        when(camionRepository.findById(camionId)).thenReturn(java.util.Optional.of(camion));
        when(camionRepository.existsByImmatriculationAndIdNot("DEF-456", camionId)).thenReturn(false);
        when(camionRepository.save(camion)).thenReturn(camion);
        when(camionMapper.toDto(camion)).thenReturn(camionDto);
        CamionDto result = camionService.modifierCamion(camionId, request);
        assertNotNull(result);
        assertEquals("DEF-456", request.getImmatriculation()  );

        verify(camionRepository) .findById(camionId);

        verify(camionRepository) .existsByImmatriculationAndIdNot(  "DEF-456",   camionId );

        verify(camionMapper)
                .updateEntityFromDto(request, camion);

        verify(camionRepository)
                .save(camion);

        verify(camionMapper)
                .toDto(camion);
    }


    @Test
    void supprimerCamion_shouldDeleteCamionSuccessfully() {

        Long camionId = 1L;

        Camion camion = new Camion();
        camion.setId(camionId);

        when(camionRepository.findById(camionId)).thenReturn(java.util.Optional.of(camion));

        camionService.supprimerCamion(camionId);
        verify(camionRepository).findById(camionId);

        verify(camionRepository).delete(camion);
    }
}