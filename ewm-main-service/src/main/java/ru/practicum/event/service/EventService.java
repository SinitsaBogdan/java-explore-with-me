package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.event.util.EventSort;
import ru.practicum.event.util.EventState;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto getByIdByInitiator(long userId, long eventId);

    EventFullDto getByIdPublic(long eventId, HttpServletRequest request);

    List<EventFullDto> getAllByAdmin(
            List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, int from, int size
    );

    List<EventShortDto> getAllByInitiator(long userId, int from, int size);

    List<ParticipationRequestDto> getParticipationRequestsByInitiator(long userId, long eventId);

    List<EventShortDto> getAllPublic(
            String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, boolean onlyAvailable, EventSort sort,
            int from, int size, HttpServletRequest request
    );

    EventFullDto add(long userId, EventNewDto eventNewDto);

    EventFullDto updateByAdmin(long eventId, EventRequestUpdateDto eventRequestUpdateDto);

    EventFullDto updateByInitiator(long userId, long eventId, EventRequestUpdateDto eventRequestUpdateDto);

    EventResultUpdateStatusDto updateParticipationRequestsByInitiator(
            long userId, long eventId, EventRequestUpdateStatusDto eventRequestUpdateStatusDto
    );
}