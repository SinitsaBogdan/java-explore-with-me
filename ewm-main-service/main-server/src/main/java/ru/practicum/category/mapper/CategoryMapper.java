package ru.practicum.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toDto(Category category);

    Category fromDto(CategoryDto categoryDto);
}