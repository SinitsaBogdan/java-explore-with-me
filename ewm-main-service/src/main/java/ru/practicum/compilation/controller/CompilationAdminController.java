package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationNewDto;
import ru.practicum.compilation.dto.CompilationRequestUpdateDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody CompilationNewDto compilationNewDto) {
        log.info("\nPOST [http://localhost:8080/admin/compilations] : запрос на создание подборки событий {}\n", compilationNewDto);
        return compilationService.add(compilationNewDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patch(
            @PathVariable @Min(0) long compId,
            @Valid @RequestBody CompilationRequestUpdateDto compilationRequestUpdateDto
    ) {
        log.info("\nPATCH [http://localhost:8080/admin/compilations/{}] : запрос на обновление подборки событий {} с ID {}\n", compId, compilationRequestUpdateDto, compId);
        return compilationService.update(compId, compilationRequestUpdateDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Min(0) long compId) {
        log.info("\nDELETE [http://localhost:8080/admin/compilations/{}] : запрос на удаление подборки событий с ID {}\n", compId, compId);
        compilationService.delete(compId);
    }
}