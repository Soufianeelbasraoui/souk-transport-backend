package org.fleetflow.souktransportbackend.mapper;

import org.fleetflow.souktransportbackend.dto.request.CargaisonRequestDto;
import org.fleetflow.souktransportbackend.dto.response.CargaisonDto;
import org.fleetflow.souktransportbackend.entity.Cargaison;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.List;
@Mapper(componentModel = "spring")
public interface CargaisonMapper {
    @Mapping(source = "expediteur.id", target = "expediteurId")
    @Mapping(source = "trajet.id", target = "trajetId")
    CargaisonDto toDto(Cargaison cargaison);
    List<CargaisonDto> toDtoList(List<Cargaison> cargaisons);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expediteur", ignore = true)
    @Mapping(target = "trajet", ignore = true)
    Cargaison toEntity(CargaisonRequestDto dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expediteur", ignore = true)
    @Mapping(target = "trajet", ignore = true)
    void updateEntityFromDto(CargaisonRequestDto dto, @MappingTarget Cargaison cargaison);
}