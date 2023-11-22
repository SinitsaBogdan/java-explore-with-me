package ru.practicum.participationRequest.mapper;

import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.model.ParticipationRequest;
import ru.practicum.util.exeption.CustomException;

public final class ParticipationRequestMapper {

    private ParticipationRequestMapper() {
        throw new CustomException("Это служебный класс, и его экземпляр не может быть создан");
    }

    public static ParticipationRequestDto toDto(ParticipationRequest model) {
        return ParticipationRequestDto.builder()
                .id(model.getId())
                .created(model.getCreated())
                .status(model.getStatus())
                .event(model.getEvent().getId())
                .requester(model.getUser().getId())
                .build();
    }
}