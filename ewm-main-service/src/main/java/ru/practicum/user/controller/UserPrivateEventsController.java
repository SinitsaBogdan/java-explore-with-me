package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class UserPrivateEventsController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getAllEventShort(
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        log.info("\nGET [http://localhost:8080/users/{}/events] : запрос на просмотр событий, добавленных пользователем с ID {}\n", userId, userId);
        return eventService.getAllByInitiator(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(
            @PathVariable long userId,
            @PathVariable long eventId
    ) {
        log.info("\nGET [http://localhost:8080/users/{}/events/{}] : запрос на просмотр события {}, добавленного пользователем с ID {}\n", userId, eventId, userId, eventId);
        return eventService.getByIdByInitiator(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestsByInitiator(
            @PathVariable long userId,
            @PathVariable long eventId
    ) {
        log.info("\nGET [http://localhost:8080/users/{}/events/{}/requests] : запрос от инициатора {} события {} на просмотр запросов на участие в событии\n", userId, eventId, userId, eventId);
        return eventService.getParticipationRequestsByInitiator(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(
            @PathVariable long userId,
            @Valid @RequestBody EventNewDto eventNewDto
    ) {
        log.info("\nPOST [http://localhost:8080/users/{}/events] : запрос на добавление события {} от пользователя с ID {}\n", userId, eventNewDto, userId);
        return eventService.add(userId, eventNewDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEventInfo(
            @PathVariable long userId,
            @PathVariable long eventId,
            @Valid @RequestBody EventRequestUpdateDto eventRequestUpdateDto
    ) {
        log.info("PATCH [http://localhost:8080/users/{}/events/{}] : запрос на обновление события {} пользователем с ID {}\n", userId, eventId, eventRequestUpdateDto, userId);
        return eventService.updateByInitiator(userId, eventId, eventRequestUpdateDto);
    }

    @PatchMapping("/{eventId}/requests")
    public EventResultUpdateStatusDto patchEventRequests(
            @PathVariable long userId,
            @PathVariable long eventId,
            @Valid @RequestBody EventRequestUpdateStatusDto eventRequestUpdateDto
    ) {
        log.info("\nPATCH [http://localhost:8080/users/{}/events/{}/requests] : запрос на обновление статусов запросов на участие в событии {}\n", userId, eventId, eventRequestUpdateDto, userId);
        return eventService.updateParticipationRequestsByInitiator(userId, eventId, eventRequestUpdateDto);
    }
}