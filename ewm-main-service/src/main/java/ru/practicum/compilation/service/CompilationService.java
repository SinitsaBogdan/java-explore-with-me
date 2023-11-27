package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationNewDto;
import ru.practicum.compilation.dto.CompilationRequestUpdateDto;

import java.util.List;

public interface CompilationService {

    CompilationDto getById(long compId);

    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto add(CompilationNewDto compilationNewDto);

    CompilationDto update(long compId, CompilationRequestUpdateDto compilationRequestUpdateDto);

    void delete(long compId);
}