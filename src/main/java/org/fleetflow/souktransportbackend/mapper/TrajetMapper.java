package org.fleetflow.souktransportbackend.mapper;

import org.fleetflow.souktransportbackend.dto.request.TrajetRequestDto;
import org.fleetflow.souktransportbackend.dto.response.TrajetDto;
import org.fleetflow.souktransportbackend.entity.Trajet;
import org.mapstruct.*;
import java.util.List;
@Mapper(componentModel = "spring")
public interface TrajetMapper {

    @Mapping(target = "typeCamion", source = "camion.type")
    @Mapping( target = "camionId", source = "camion.id" )
    TrajetDto toDto(Trajet trajet);
    List<TrajetDto> toDtoList(List<Trajet> trajets);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "camion", ignore = true)
    @Mapping(target = "cargaisons", ignore = true)
    Trajet toEntityRequest(TrajetRequestDto dto);

    @BeanMapping( nullValuePropertyMappingStrategy =NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "camion", ignore = true)
    @Mapping(target = "cargaisons", ignore = true)
    void updateEntityFromDto(TrajetRequestDto dto, @MappingTarget Trajet trajet);
}