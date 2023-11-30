package ru.practicum.compilation.mapper;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationNewDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.util.exeption.CustomException;

import java.util.List;
import java.util.stream.Collectors;

public final class CompilationMapper {

    private CompilationMapper() {
        throw new CustomException("Это служебный класс, и его экземпляр не может быть создан");
    }

    public static Compilation fromDto(CompilationNewDto dto, List<Event> events) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .events(events)
                .build();
    }

    public static CompilationDto toDto(Compilation model) {
        return CompilationDto.builder()
                .id(model.getId())
                .title(model.getTitle())
                .pinned(model.getPinned())
                .events(model.getEvents().stream().map(EventMapper::toShortDto).collect(Collectors.toList()))
                .build();
    }
}