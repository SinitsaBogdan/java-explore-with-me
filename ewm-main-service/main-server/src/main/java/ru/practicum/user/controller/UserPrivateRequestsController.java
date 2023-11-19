package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.service.ParticipationRequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class UserPrivateRequestsController {

    private final ParticipationRequestService participationRequestService;

    @GetMapping
    public List<ParticipationRequestDto> getAll(@PathVariable long userId) {
        log.info("   GET [http://localhost:8080/users/{}/requests] : запрос на просмотр запросов пользователя с ID {} на участие в событиях", userId, userId);
        return participationRequestService.getAll(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(
            @PathVariable long userId,
            @RequestParam long eventId
    ) {
        log.info("  POST [http://localhost:8080/users/{}/requests] : запрос на создание запроса на участие в событии {} от пользователя {}", userId, eventId, userId);
        return participationRequestService.create(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto patch(
            @PathVariable long userId,
            @PathVariable long requestId
    ) {
        log.info(" PATCH [http://localhost:8080/users/{}/requests/{}/cancel] : запрос на отмену запроса {} на участие в событии пользователем с ID {}", userId, requestId, userId, requestId);
        return participationRequestService.patch(userId, requestId);
    }
}
