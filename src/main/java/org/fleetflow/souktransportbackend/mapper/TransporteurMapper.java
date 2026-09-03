package org.fleetflow.souktransportbackend.mapper;

import org.fleetflow.souktransportbackend.dto.request.TransporteurRequestDto;
import org.fleetflow.souktransportbackend.dto.response.TransporteurDto;
import org.fleetflow.souktransportbackend.entity.Transporteur;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TransporteurMapper {

    TransporteurDto toDto(Transporteur entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "camions", ignore = true)
    Transporteur toEntity(TransporteurDto dto);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "camions", ignore = true)
    Transporteur toEntityRequest(TransporteurRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "camions", ignore = true)
    void updateEntityFromDto(TransporteurRequestDto dto, @MappingTarget Transporteur entity);
}
