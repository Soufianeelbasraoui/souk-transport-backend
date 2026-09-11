package org.fleetflow.souktransportbackend.mapper;

import org.fleetflow.souktransportbackend.dto.request.ReservationRequestDto;
import org.fleetflow.souktransportbackend.dto.response.ReservationDto;
import org.fleetflow.souktransportbackend.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "trajetId", source = "trajet.id")
    @Mapping(target = "cargaisonId", source = "cargaison.id")
    ReservationDto toDto(Reservation entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateReservation", ignore = true)
    @Mapping(target = "trajet", ignore = true)
    @Mapping(target = "cargaison", ignore = true)
    @Mapping(target = "paiement", ignore = true)
    Reservation toEntity(ReservationRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateReservation", ignore = true)
    @Mapping(target = "trajet", ignore = true)
    @Mapping(target = "cargaison", ignore = true)
    @Mapping(target = "paiement", ignore = true)
    void updateEntityFromDto(ReservationRequestDto dto, @MappingTarget Reservation entity);
}