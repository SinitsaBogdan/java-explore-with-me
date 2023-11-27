package ru.practicum.participationRequest.service;

import ru.practicum.participationRequest.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    List<ParticipationRequestDto> getAll(long userId);

    ParticipationRequestDto add(long userId, long eventId);

    ParticipationRequestDto update(long userId, long requestId);
}