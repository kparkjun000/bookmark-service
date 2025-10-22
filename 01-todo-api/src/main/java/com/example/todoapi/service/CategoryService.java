package com.example.todoapi.service;

import com.example.todoapi.dto.CategoryDto;
import com.example.todoapi.entity.Category;
import com.example.todoapi.entity.User;
import com.example.todoapi.repository.CategoryRepository;
import com.example.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final TodoRepository todoRepository;

    public CategoryDto.CategoryResponse createCategory(User user, CategoryDto.CreateRequest request) {
        if (categoryRepository.existsByNameAndUser(request.getName(), user)) {
            throw new RuntimeException("Category with this name already exists");
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .color(request.getColor())
                .user(user)
                .build();

        category = categoryRepository.save(category);
        return mapToResponse(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto.CategoryResponse> getAllCategories(User user) {
        return categoryRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto.CategoryResponse getCategoryById(User user, Long categoryId) {
        Category category = categoryRepository.findByIdAndUser(categoryId, user)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return mapToResponse(category);
    }

    public CategoryDto.CategoryResponse updateCategory(User user, Long categoryId, CategoryDto.UpdateRequest request) {
        Category category = categoryRepository.findByIdAndUser(categoryId, user)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (request.getName() != null) {
            if (!category.getName().equals(request.getName()) &&
                categoryRepository.existsByNameAndUser(request.getName(), user)) {
                throw new RuntimeException("Category with this name already exists");
            }
            category.setName(request.getName());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getColor() != null) {
            category.setColor(request.getColor());
        }

        category = categoryRepository.save(category);
        return mapToResponse(category);
    }

    public void deleteCategory(User user, Long categoryId) {
        Category category = categoryRepository.findByIdAndUser(categoryId, user)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }

    public CategoryDto.CategoryResponse mapToResponse(Category category) {
        Long todoCount = todoRepository.countByCategoryId(category.getUser(), category.getId());

        return CategoryDto.CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .color(category.getColor())
                .todoCount(todoCount.intValue())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}