package ru.practicum.event.mapper;

import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventNewDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.util.exeption.CustomException;

public final class EventMapper {

    private EventMapper() {
        throw new CustomException("Это служебный класс, и его экземпляр не может быть создан");
    }

    public static Event fromDto(EventNewDto dto, Category category, Location location) {
        return Event.builder()
                .category(category)
                .location(location)
                .title(dto.getTitle())
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .eventDate(dto.getEventTimestamp())
                .participantLimit(dto.getParticipantLimit())
                .paid(dto.getPaid())
                .requestModeration(dto.getRequestModeration())
                .build();
    }

    public static EventFullDto toFullDto(Event model) {
        return EventFullDto.builder()
                .id(model.getId())
                .initiator(UserMapper.toShortDto(model.getInitiator()))
                .category(CategoryMapper.toDto(model.getCategory()))
                .location(LocationMapper.toDto(model.getLocation()))
                .title(model.getTitle())
                .annotation(model.getAnnotation())
                .description(model.getDescription())
                .state(model.getState())
                .eventDate(model.getEventDate())
                .createdOn(model.getCreatedOn())
                .publishedOn(model.getPublishedOn())
                .participantLimit(model.getParticipantLimit())
                .requestModeration(model.getRequestModeration())
                .paid(model.getPaid())
                .build();
    }

    public static EventShortDto toShortDto(Event model) {
        return EventShortDto.builder()
                .id(model.getId())
                .initiator(UserMapper.toShortDto(model.getInitiator()))
                .category(CategoryMapper.toDto(model.getCategory()))
                .title(model.getTitle())
                .annotation(model.getAnnotation())
                .eventDate(model.getEventDate())
                .paid(model.getPaid())
                .build();
    }
}