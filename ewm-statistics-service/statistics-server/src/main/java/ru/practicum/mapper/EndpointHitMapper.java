package ru.practicum.mapper;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

import javax.validation.constraints.NotNull;

public final class EndpointHitMapper {

    private EndpointHitMapper() {
    }

    public static EndpointHitDto mapperEntityToDto(@NotNull EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .endpointTimestamp(endpointHit.getTimestamp())
                .build();
    }

    public static EndpointHit mapperDtoToEntity(@NotNull EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .uri(endpointHitDto.getUri())
                .app(endpointHitDto.getApp())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getEndpointTimestamp())
                .build();
    }
}

