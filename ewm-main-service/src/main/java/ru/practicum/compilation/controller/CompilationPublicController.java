package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationPublicController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getAll(
            @RequestParam(required = false) Boolean pinned,
            @Valid @RequestParam(defaultValue = "0") @Min(0) int from,
            @Valid @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        log.info("\nGET [http://localhost:8080/compilations] : запрос на просмотр подборок событий\n");
        return compilationService.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable long compId) {
        log.info("\nGET [http://localhost:8080/compilations/{}] : запрос для подборки событий по ID {}\n", compId, compId);
        return compilationService.getById(compId);
    }
}