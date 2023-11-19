package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repo.CategoryRepository;
import ru.practicum.util.exeption.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAll(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from, size))
                .getContent().stream()
                .map(CategoryMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getById(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id " + catId + " не найдена!"));

        return CategoryMapper.INSTANCE.toDto(category);
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        Category category = CategoryMapper.INSTANCE.fromDto(categoryDto);
        Category saveCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.toDto(saveCategory);
    }

    @Override
    public CategoryDto patch(long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id " + catId + " не найдена!"));

        Optional.ofNullable(categoryDto.getName()).ifPresent(category::setName);
        Category saveCategory = categoryRepository.save(category);

        return CategoryMapper.INSTANCE.toDto(saveCategory);

    }

    @Override
    public void delete(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id " + catId + " не найдена!"));

        categoryRepository.deleteById(catId);
    }
}
