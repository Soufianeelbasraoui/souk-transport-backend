package org.fleetflow.souktransportbackend.mapper;

import org.fleetflow.souktransportbackend.dto.request.ExpediteurRequestDto;
import org.fleetflow.souktransportbackend.dto.response.ExpediteurDto;
import org.fleetflow.souktransportbackend.entity.Expediteur;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ExpediteurMapper {

    ExpediteurDto toDto(Expediteur entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cargaisons", ignore = true)
    Expediteur toEntity(ExpediteurDto dto);
    ExpediteurRequestDto toDtoRequest(Expediteur entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cargaisons", ignore = true)
    Expediteur toEntityRequest(ExpediteurRequestDto dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cargaisons", ignore = true)
    void updateEntityFromDto(ExpediteurRequestDto dto, @MappingTarget Expediteur entity);
}
