package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto getById(long catId);

    List<CategoryDto> getAll(int from, int size);

    CategoryDto add(CategoryDto categoryDto);

    CategoryDto update(long catId, CategoryDto categoryDto);

    void delete(long catId);
}
