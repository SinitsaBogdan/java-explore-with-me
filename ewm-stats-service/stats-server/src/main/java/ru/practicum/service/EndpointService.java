package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewEndpointDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointService {

    EndpointHit add(EndpointHitDto endpointHit);

    List<ViewEndpointDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}