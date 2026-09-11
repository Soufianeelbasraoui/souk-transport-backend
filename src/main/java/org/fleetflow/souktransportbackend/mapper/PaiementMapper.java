package org.fleetflow.souktransportbackend.mapper;

import org.fleetflow.souktransportbackend.dto.request.PaiementRequestDto;
import org.fleetflow.souktransportbackend.dto.response.PaiementDto;
import org.fleetflow.souktransportbackend.entity.Paiement;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaiementMapper {

    @Mapping(target = "reservationId", source = "reservation.id")
    PaiementDto toDto(Paiement paiement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    Paiement toEntity(PaiementRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    void updateEntityFromDto(PaiementRequestDto dto, @MappingTarget Paiement paiement);
}