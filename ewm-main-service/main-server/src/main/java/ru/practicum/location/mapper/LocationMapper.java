package ru.practicum.location.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;

@Mapper
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    LocationDto toDto(Location entity);

    Location fromDto(LocationDto dto);
}
