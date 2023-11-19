package ru.practicum.mapper;

import org.mapstruct.factory.Mappers;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

public interface EndpointHitMapper {

    EndpointHitMapper INSTANCE = Mappers.getMapper(EndpointHitMapper.class);

    EndpointHitDto toDto(EndpointHit user);

    EndpointHit fromDto(EndpointHitDto userDto);
}

