package ru.practicum.mapper;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

public final class EndpointHitMapper {

    private EndpointHitMapper() {
    }

    public static EndpointHitDto toDto(EndpointHit model) {
        return EndpointHitDto.builder()
                .app(model.getApp())
                .uri(model.getUri())
                .ip(model.getIp())
                .endpointTimestamp(model.getHitTimestamp())
                .build();
    }

    public static EndpointHit fromDto(EndpointHitDto dto) {
        return EndpointHit.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .hitTimestamp(dto.getEndpointTimestamp())
                .build();
    }
}