package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationNewDto;
import ru.practicum.compilation.dto.CompilationRequestUpdateDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody CompilationNewDto compilationNewDto) {
        log.info("  POST [http://localhost:8080/admin/compilations] : запрос на создание подборки событий {}", compilationNewDto);
        return compilationService.create(compilationNewDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patch(
            @PathVariable long compId,
            @Valid @RequestBody CompilationRequestUpdateDto compilationRequestUpdateDto
    ) {
        log.info(" PATCH [http://localhost:8080/admin/compilations/{}] : запрос на обновление подборки событий {} с ID {}", compId, compilationRequestUpdateDto, compId);
        return compilationService.patch(compId, compilationRequestUpdateDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long compId) {
        log.info("DELETE [http://localhost:8080/admin/compilations/{}] : запрос на удаление подборки событий с ID {}", compId, compId);
        compilationService.delete(compId);
    }
}