package org.fleetflow.souktransportbackend.service.impl;

import org.fleetflow.souktransportbackend.dto.response.CargaisonDto;
import org.fleetflow.souktransportbackend.entity.Cargaison;
import org.fleetflow.souktransportbackend.mapper.CargaisonMapper;
import org.fleetflow.souktransportbackend.repository.CargaisonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CargaisonServiceImplTest {

    @Mock
    private CargaisonRepository cargaisonRepository;

    @Mock
    private CargaisonMapper cargaisonMapper;

    @InjectMocks
    private CargaisonServiceImpl cargaisonService;

    @Test
    void consulterCargaison_shouldReturnCargaison() {

        Long id = 1L;

        Cargaison cargaison = new Cargaison();
        cargaison.setId(id);

        CargaisonDto cargaisonDto = new CargaisonDto();

        when(cargaisonRepository.findById(id)).thenReturn(Optional.of(cargaison));
        when(cargaisonMapper.toDto(cargaison)) .thenReturn(cargaisonDto);
        CargaisonDto result = cargaisonService.consulterCargaison(id);
        assertNotNull(result);
        verify(cargaisonRepository) .findById(id);

        verify(cargaisonMapper).toDto(cargaison);
    }
    @Test
    void listerCargaisons_shouldReturnPageOfCargaisons() {

        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        Cargaison cargaison = new Cargaison();
        cargaison.setId(1L);

        CargaisonDto cargaisonDto = new CargaisonDto();

        Page<Cargaison> cargaisonPage =new PageImpl<>( List.of(cargaison), pageable, 1);

        when(cargaisonRepository.findAll(pageable)).thenReturn(cargaisonPage);

        when(cargaisonMapper.toDto(cargaison)).thenReturn(cargaisonDto);

        Page<CargaisonDto> result = cargaisonService.listerCargaisons(page, size);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        assertEquals(1, result.getContent().size());

        verify(cargaisonRepository)
                .findAll(pageable);

        verify(cargaisonMapper)
                .toDto(cargaison);
    }

    @Test
    void rechercherParDescription_shouldReturnMatchingCargaisons() {

        String description = "Meubles";

        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);

        Cargaison cargaison = new Cargaison();
        cargaison.setId(1L);
        cargaison.setDescription("Meubles pour maison");

        CargaisonDto cargaisonDto = new CargaisonDto();

        Page<Cargaison> cargaisonPage = new PageImpl<>(  List.of(cargaison), pageable,1 );

        when(cargaisonRepository.findByDescriptionContainingIgnoreCase( description, pageable )).thenReturn(cargaisonPage);
        when(cargaisonMapper.toDto(cargaison)) .thenReturn(cargaisonDto);

        Page<CargaisonDto> result = cargaisonService.rechercherParDescription( description,page,  size );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        verify(cargaisonRepository).findByDescriptionContainingIgnoreCase( description,  pageable  );

        verify(cargaisonMapper) .toDto(cargaison);
    }
}