package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.util.EventSort;
import ru.practicum.util.Constants;
import ru.practicum.client.StatisticsClient;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;
import ru.practicum.util.exeption.RequestException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventPublicController {

    private final EventService eventService;

    @Value("${STAT_SERVER_URL:http://localhost:9090}")
    private String statClientUrl;

    private StatisticsClient client;

    @PostConstruct
    private void init() {
        client = new StatisticsClient(statClientUrl);
    }

    @GetMapping
    public List<EventShortDto> getAll(
            @RequestParam(defaultValue = "") @Size(max = 2000) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_TEMPLATE) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_TEMPLATE) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
            @RequestParam(defaultValue = "VIEWS") EventSort sort,
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            HttpServletRequest request
    ) {

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new RequestException("Дата начала должна быть раньше даты окончания");
        }

        client.saveHit(EndpointHitDto.builder()
                .app("ewm")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .endpointTimestamp(LocalDateTime.now())
                .build());

        log.info("\nGET [http://localhost:8080/events] : запрос на просмотр событий по фильтрам\n");
        return eventService.getAllPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(
            @PathVariable long eventId,
            HttpServletRequest request
    ) {
        client.saveHit(EndpointHitDto.builder()
                .app("ewm")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .endpointTimestamp(LocalDateTime.now())
                .build());

        log.info("\nGET [http://localhost:8080/events/{}] : запрос на просмотр события по ID {}\n", eventId, eventId);
        return eventService.getByIdPublic(eventId, request);
    }
}