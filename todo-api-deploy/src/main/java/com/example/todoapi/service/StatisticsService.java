package com.example.todoapi.service;

import com.example.todoapi.dto.StatisticsDto;
import com.example.todoapi.entity.Todo;
import com.example.todoapi.entity.User;
import com.example.todoapi.repository.CategoryRepository;
import com.example.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {
    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;

    public StatisticsDto getStatistics(User user) {
        Long totalTodos = todoRepository.countByUser(user);
        Long completedTodos = todoRepository.countCompletedByUser(user);
        Long pendingTodos = totalTodos - completedTodos;
        Double completionRate = totalTodos > 0 ? (completedTodos * 100.0) / totalTodos : 0.0;

        Map<String, Long> todosByPriority = getTodosByPriority(user);
        Map<String, Long> todosByCategory = getTodosByCategory(user);
        Map<String, Double> completionRateByCategory = getCompletionRateByCategory(user);

        LocalDateTime now = LocalDateTime.now();
        Long overdueTodos = todoRepository.findOverdueTodos(user, now).stream().count();

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        Long todayTodos = todoRepository.findByUserAndDueDateBetween(user, startOfDay, endOfDay).stream().count();

        LocalDateTime startOfWeek = LocalDate.now().atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusWeeks(1);
        Long thisWeekTodos = todoRepository.findByUserAndDueDateBetween(user, startOfWeek, endOfWeek).stream().count();

        return StatisticsDto.builder()
                .totalTodos(totalTodos)
                .completedTodos(completedTodos)
                .pendingTodos(pendingTodos)
                .completionRate(completionRate)
                .todosByPriority(todosByPriority)
                .todosByCategory(todosByCategory)
                .completionRateByCategory(completionRateByCategory)
                .overdueTodos(overdueTodos)
                .todayTodos(todayTodos)
                .thisWeekTodos(thisWeekTodos)
                .build();
    }

    private Map<String, Long> getTodosByPriority(User user) {
        Map<String, Long> todosByPriority = new HashMap<>();
        List<Todo> todos = todoRepository.findByUser(user, null).getContent();

        for (Todo.Priority priority : Todo.Priority.values()) {
            long count = todos.stream()
                    .filter(todo -> todo.getPriority() == priority)
                    .count();
            todosByPriority.put(priority.name(), count);
        }

        return todosByPriority;
    }

    private Map<String, Long> getTodosByCategory(User user) {
        Map<String, Long> todosByCategory = new HashMap<>();
        List<Object[]> results = categoryRepository.findCategoriesWithTodoCount(user);

        for (Object[] result : results) {
            String categoryName = (String) result[1];
            Long todoCount = (Long) result[2];
            todosByCategory.put(categoryName, todoCount);
        }

        List<Todo> todosWithoutCategory = todoRepository.findByUserAndCategoryId(user, null);
        if (!todosWithoutCategory.isEmpty()) {
            todosByCategory.put("Uncategorized", (long) todosWithoutCategory.size());
        }

        return todosByCategory;
    }

    private Map<String, Double> getCompletionRateByCategory(User user) {
        Map<String, Double> completionRates = new HashMap<>();
        List<Object[]> categories = categoryRepository.findCategoriesWithTodoCount(user);

        for (Object[] category : categories) {
            Long categoryId = (Long) category[0];
            String categoryName = (String) category[1];
            List<Todo> categoryTodos = todoRepository.findByUserAndCategoryId(user, categoryId);

            if (!categoryTodos.isEmpty()) {
                long completed = categoryTodos.stream().filter(Todo::getCompleted).count();
                double rate = (completed * 100.0) / categoryTodos.size();
                completionRates.put(categoryName, rate);
            }
        }

        return completionRates;
    }
}