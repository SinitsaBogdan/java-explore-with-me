package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("\nPOST [http://localhost:8080/categories] : запрос на добавление категории {}\n", categoryDto);
        return categoryService.add(categoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto patch(
            @PathVariable @Min(0) long catId,
            @Valid @RequestBody CategoryDto categoryDto
    ) {
        log.info("\nPATCH [http://localhost:8080/categories/{}] : запрос на обновление категории {} c ID {}\n", catId, categoryDto, catId);
        return categoryService.update(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Min(0) long catId) {
        log.info("\nDELETE [http://localhost:8080/categories/{}] : запрос на удаление категории c ID {}\n", catId, catId);
        categoryService.delete(catId);
    }
}