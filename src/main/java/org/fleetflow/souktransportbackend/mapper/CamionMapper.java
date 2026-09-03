package org.fleetflow.souktransportbackend.mapper;

import org.fleetflow.souktransportbackend.dto.request.CamionRequestDto;
import org.fleetflow.souktransportbackend.dto.response.CamionDto;
import org.fleetflow.souktransportbackend.entity.Camion;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CamionMapper {
    @Mapping(source = "transporteur.id", target = "transporteurId")
    CamionDto toDto(Camion camion);
    List<CamionDto> toDtoList(List<Camion> camions);

    @Mapping(source = "transporteurId", target = "transporteur.id")
    Camion toEntityRequest(CamionRequestDto dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "transporteurId", target = "transporteur.id")
    void updateEntityFromDto(CamionRequestDto dto, @MappingTarget Camion camion);
}