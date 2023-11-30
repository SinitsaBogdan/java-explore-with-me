package ru.practicum.location.mapper;

import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;
import ru.practicum.util.exeption.CustomException;

public final class LocationMapper {

    private LocationMapper() {
        throw new CustomException("Это служебный класс, и его экземпляр не может быть создан");
    }

    public static LocationDto toDto(Location model) {
        return LocationDto.builder()
                .lat(model.getLat())
                .lon(model.getLon())
                .build();
    }

    public static Location fromDto(LocationDto dto) {
        return Location.builder()
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }
}