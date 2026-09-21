package org.fleetflow.souktransportbackend.service.impl;

import org.fleetflow.souktransportbackend.dto.request.TrajetRequestDto;
import org.fleetflow.souktransportbackend.dto.response.TrajetDto;
import org.fleetflow.souktransportbackend.entity.Camion;
import org.fleetflow.souktransportbackend.entity.Trajet;
import org.fleetflow.souktransportbackend.mapper.TrajetMapper;
import org.fleetflow.souktransportbackend.repository.CamionRepository;
import org.fleetflow.souktransportbackend.repository.TrajetRepository;
import org.fleetflow.souktransportbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrajetServiceImplTest {

    @Mock
    private TrajetRepository trajetRepository;

    @Mock
    private CamionRepository camionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrajetMapper trajetMapper;

    @InjectMocks
    private TrajetServiceImpl trajetService;

    @Test
    void ajouterTrajetCamionDisponibleToFalse() {
        Long camionId = 1L;
        TrajetRequestDto request = new TrajetRequestDto("Casablanca", "Rabat", LocalDateTime.now().plusDays(1), 500.0, 1000.0, camionId);

        Camion camion = new Camion();
        camion.setId(camionId);
        camion.setDisponible(true);

        Trajet trajet = new Trajet();
        trajet.setCamion(camion);

        TrajetDto trajetDto = new TrajetDto();

        when(camionRepository.findById(camionId)).thenReturn(Optional.of(camion));
        when(trajetMapper.toEntityRequest(request)).thenReturn(trajet);
        when(trajetRepository.save(trajet)).thenReturn(trajet);
        when(trajetMapper.toDto(trajet)).thenReturn(trajetDto);

        TrajetDto result = trajetService.ajouterTrajet(request);

        assertNotNull(result);
        assertFalse(camion.getDisponible());
        verify(camionRepository).save(camion);
        verify(trajetRepository).save(trajet);
    }

    @Test
    void ajouterTrajetCamionNotDisponible() {
        Long camionId = 1L;
        TrajetRequestDto request = new TrajetRequestDto("Casablanca", "Rabat", LocalDateTime.now().plusDays(1), 500.0, 1000.0, camionId);

        Camion camion = new Camion();
        camion.setId(camionId);
        camion.setDisponible(false);

        when(camionRepository.findById(camionId)).thenReturn(Optional.of(camion));

        assertThrows(RuntimeException.class, () -> trajetService.ajouterTrajet(request));
        verify(trajetRepository, never()).save(any());
    }

    @Test
    void modifierTrajet() {
        Long trajetId = 10L;
        Long ancienCamionId = 1L;
        Long nouveauCamionId = 2L;

        TrajetRequestDto request = new TrajetRequestDto();
        request.setCamionId(nouveauCamionId);

        Camion ancienCamion = new Camion();
        ancienCamion.setId(ancienCamionId);
        ancienCamion.setDisponible(false);

        Camion nouveauCamion = new Camion();
        nouveauCamion.setId(nouveauCamionId);
        nouveauCamion.setDisponible(true);

        Trajet trajet = new Trajet();
        trajet.setId(trajetId);
        trajet.setCamion(ancienCamion);

        TrajetDto trajetDto = new TrajetDto();

        when(trajetRepository.findById(trajetId)).thenReturn(Optional.of(trajet));
        when(camionRepository.findById(nouveauCamionId)).thenReturn(Optional.of(nouveauCamion));
        when(trajetRepository.save(trajet)).thenReturn(trajet);
        when(trajetMapper.toDto(trajet)).thenReturn(trajetDto);

        TrajetDto result = trajetService.modifierTrajet(trajetId, request);

        assertNotNull(result);
        assertTrue(ancienCamion.getDisponible());
        assertFalse(nouveauCamion.getDisponible());
        verify(camionRepository).save(ancienCamion);
        verify(camionRepository).save(nouveauCamion);
        assertEquals(nouveauCamion, trajet.getCamion());
    }

    @Test
    void supprimerTrajetTrue() {
        Long trajetId = 10L;
        Camion camion = new Camion();
        camion.setId(1L);
        camion.setDisponible(false);

        Trajet trajet = new Trajet();
        trajet.setId(trajetId);
        trajet.setCamion(camion);

        when(trajetRepository.findById(trajetId)).thenReturn(Optional.of(trajet));

        trajetService.supprimerTrajet(trajetId);

        assertTrue(camion.getDisponible());
        verify(camionRepository).save(camion);
        verify(trajetRepository).delete(trajet);
    }
}
