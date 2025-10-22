package com.example.todoapi.service;

import com.example.todoapi.dto.TodoDto;
import com.example.todoapi.entity.Category;
import com.example.todoapi.entity.Todo;
import com.example.todoapi.entity.User;
import com.example.todoapi.repository.CategoryRepository;
import com.example.todoapi.repository.TodoRepository;
import com.example.todoapi.specification.TodoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {
    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public TodoDto.TodoResponse createTodo(User user, TodoDto.CreateRequest request) {
        Todo todo = Todo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .completed(false)
                .user(user)
                .build();

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndUser(request.getCategoryId(), user)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            todo.setCategory(category);
        }

        todo = todoRepository.save(todo);
        return mapToResponse(todo);
    }

    @Transactional(readOnly = true)
    public Page<TodoDto.TodoResponse> getAllTodos(User user, Pageable pageable) {
        return todoRepository.findByUser(user, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public TodoDto.TodoResponse getTodoById(User user, Long todoId) {
        Todo todo = todoRepository.findByIdAndUser(todoId, user)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        return mapToResponse(todo);
    }

    public TodoDto.TodoResponse updateTodo(User user, Long todoId, TodoDto.UpdateRequest request) {
        Todo todo = todoRepository.findByIdAndUser(todoId, user)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        if (request.getTitle() != null) {
            todo.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            todo.setDescription(request.getDescription());
        }
        if (request.getCompleted() != null) {
            todo.setCompleted(request.getCompleted());
        }
        if (request.getPriority() != null) {
            todo.setPriority(request.getPriority());
        }
        if (request.getDueDate() != null) {
            todo.setDueDate(request.getDueDate());
        }
        if (request.getCategoryId() != null) {
            if (request.getCategoryId() == 0) {
                todo.setCategory(null);
            } else {
                Category category = categoryRepository.findByIdAndUser(request.getCategoryId(), user)
                        .orElseThrow(() -> new RuntimeException("Category not found"));
                todo.setCategory(category);
            }
        }

        todo = todoRepository.save(todo);
        return mapToResponse(todo);
    }

    public void deleteTodo(User user, Long todoId) {
        Todo todo = todoRepository.findByIdAndUser(todoId, user)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        todoRepository.delete(todo);
    }

    @Transactional(readOnly = true)
    public List<TodoDto.TodoResponse> searchTodos(User user, TodoDto.SearchRequest searchRequest) {
        Specification<Todo> spec = Specification.where(TodoSpecification.belongsToUser(user));

        if (searchRequest.getKeyword() != null && !searchRequest.getKeyword().isEmpty()) {
            spec = spec.and(TodoSpecification.containsKeyword(searchRequest.getKeyword()));
        }
        if (searchRequest.getCompleted() != null) {
            spec = spec.and(TodoSpecification.isCompleted(searchRequest.getCompleted()));
        }
        if (searchRequest.getPriority() != null) {
            spec = spec.and(TodoSpecification.hasPriority(searchRequest.getPriority()));
        }
        if (searchRequest.getCategoryId() != null) {
            spec = spec.and(TodoSpecification.hasCategory(searchRequest.getCategoryId()));
        }
        if (searchRequest.getDueDateFrom() != null && searchRequest.getDueDateTo() != null) {
            spec = spec.and(TodoSpecification.dueDateBetween(searchRequest.getDueDateFrom(), searchRequest.getDueDateTo()));
        }

        return todoRepository.findAll(spec).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TodoDto.TodoResponse> getTodosByStatus(User user, Boolean completed) {
        return todoRepository.findByUserAndCompleted(user, completed).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TodoDto.TodoResponse> getTodosByPriority(User user, Todo.Priority priority) {
        return todoRepository.findByUserAndPriority(user, priority).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TodoDto.TodoResponse mapToResponse(Todo todo) {
        TodoDto.TodoResponse response = TodoDto.TodoResponse.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.getCompleted())
                .priority(todo.getPriority())
                .dueDate(todo.getDueDate())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();

        if (todo.getCategory() != null) {
            response.setCategory(categoryService.mapToResponse(todo.getCategory()));
        }

        return response;
    }
}