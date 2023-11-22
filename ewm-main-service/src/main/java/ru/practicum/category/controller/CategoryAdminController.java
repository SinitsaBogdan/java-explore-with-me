package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("  POST [http://localhost:8080/categories] : запрос на добавление категории {}", categoryDto);
        return categoryService.create(categoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto patch(
            @PathVariable long catId,
            @Valid @RequestBody CategoryDto categoryDto
    ) {
        log.info(" PATCH [http://localhost:8080/categories/{}] : запрос на обновление категории {} c ID {}", catId, categoryDto, catId);
        return categoryService.patch(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long catId) {
        log.info("DELETE [http://localhost:8080/categories/{}] : запрос на удаление категории c ID {}", catId, catId);
        categoryService.delete(catId);
    }
}
