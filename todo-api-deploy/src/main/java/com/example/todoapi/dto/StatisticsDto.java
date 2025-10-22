package com.example.todoapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDto {
    private Long totalTodos;
    private Long completedTodos;
    private Long pendingTodos;
    private Double completionRate;
    private Map<String, Long> todosByPriority;
    private Map<String, Long> todosByCategory;
    private Map<String, Double> completionRateByCategory;
    private Long overdueTodos;
    private Long todayTodos;
    private Long thisWeekTodos;
}