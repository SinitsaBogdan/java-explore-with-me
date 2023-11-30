package ru.practicum.participationRequest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.service.ParticipationRequestService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class ParticipationRequestPrivateController {

    private final ParticipationRequestService service;

    @GetMapping
    public List<ParticipationRequestDto> getAllParticipationRequest(@PathVariable long userId) {
        log.info("\nGET [http://localhost:8080/users/{userId}/requests] : запрос на просмотр запросов пользователя с ID {} на участие в событиях\n", userId, userId);
        return service.getAll(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(
            @PathVariable long userId,
            @RequestParam long eventId
    ) {
        log.info("\nPOST [http://localhost:8080/users/{}/requests?eventId={}] : запрос на создание запроса на участие в событии {} от пользователя {}\n", userId, eventId, eventId, userId);
        return service.add(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto patch(
            @PathVariable long userId,
            @PathVariable long requestId
    ) {
        log.info("\nPATCH [http://localhost:8080/users/{userId}/requests] : запрос на отмену запроса {} на участие в событии пользователем с ID {}\n", userId, requestId, userId);
        return service.update(userId, requestId);
    }
}