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
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from, size))
                .getContent().stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id " + catId + " не найдена!"));

        return CategoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto add(CategoryDto categoryDto) {
        Category category = CategoryMapper.fromDto(categoryDto);
        Category saveCategory = categoryRepository.save(category);
        return CategoryMapper.toDto(saveCategory);
    }

    @Override
    @Transactional
    public CategoryDto update(long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id " + catId + " не найдена!"));

        Optional.ofNullable(categoryDto.getName()).ifPresent(category::setName);
        Category saveCategory = categoryRepository.save(category);

        return CategoryMapper.toDto(saveCategory);

    }

    @Override
    @Transactional
    public void delete(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id " + catId + " не найдена!"));

        categoryRepository.deleteById(catId);
    }
}