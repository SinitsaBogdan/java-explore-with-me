package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestUpdateDto;
import ru.practicum.event.service.EventService;
import ru.practicum.event.util.EventState;
import ru.practicum.util.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> get(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_TEMPLATE) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_TEMPLATE) LocalDateTime rangeEnd,
            @Valid @RequestParam(defaultValue = "0") @Min(0) int from,
            @Valid @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        log.info("\nGET [http://localhost:8080/admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size={}] : запрос от администратора на просмотр событий по фильтрам\n", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getAllByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patch(
            @PathVariable long eventId,
            @Valid @RequestBody EventRequestUpdateDto eventRequestUpdateDto
    ) {
        log.info("\nPATCH [http://localhost:8080/admin/events/{}] : запрос на обновление события с ID {} администратором \n{}\n", eventId, eventId, eventRequestUpdateDto);
        return eventService.patchByAdmin(eventId, eventRequestUpdateDto);
    }
}