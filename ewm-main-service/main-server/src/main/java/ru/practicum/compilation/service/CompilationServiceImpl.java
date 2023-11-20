package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationNewDto;
import ru.practicum.compilation.dto.CompilationRequestUpdateDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repo.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repo.EventRepository;
import ru.practicum.util.exeption.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        return compilationRepository.findAllByPublic(pinned, pageable).stream()
                .map(CompilationMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(long compId) {
        return CompilationMapper.INSTANCE.toDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Сборник с id " + compId + " не найден!"))
        );
    }

    @Override
    @Transactional
    public CompilationDto create(CompilationNewDto compilationNewDto) {
        List<Event> events = compilationNewDto.getEvents() != null && !compilationNewDto.getEvents().isEmpty() ?
                eventRepository.findAllById(compilationNewDto.getEvents()) : Collections.emptyList();

        if (compilationNewDto.getPinned() == null) compilationNewDto.setPinned(false);
        return CompilationMapper.INSTANCE.toDto(
                compilationRepository.save(CompilationMapper.INSTANCE.fromDto(compilationNewDto, events))
        );
    }

    @Override
    @Transactional
    public CompilationDto patch(long compId, CompilationRequestUpdateDto compilationRequestUpdateDto) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Сборник с id " + compId + " не найден!"));

        if (compilationRequestUpdateDto.getEvents() != null) compilation.setEvents(
                eventRepository.findAllById(compilationRequestUpdateDto.getEvents())
        );

        Optional.ofNullable(compilationRequestUpdateDto.getTitle()).ifPresent(compilation::setTitle);
        Optional.ofNullable(compilationRequestUpdateDto.getPinned()).ifPresent(compilation::setPinned);

        return CompilationMapper.INSTANCE.toDto(compilation);
    }

    @Override
    public void delete(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Сборник с id " + compId + " не найден!"));

        compilationRepository.deleteById(compId);
    }
}