package ru.practicum.category.mapper;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.util.exeption.CustomException;

public final class CategoryMapper {

    private CategoryMapper() {
        throw new CustomException("Это служебный класс, и его экземпляр не может быть создан");
    }

    public static CategoryDto toDto(Category model) {
        return CategoryDto.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

    public static Category fromDto(CategoryDto dto) {
        return Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}