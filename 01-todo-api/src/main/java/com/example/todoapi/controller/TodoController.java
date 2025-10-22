package com.example.todoapi.controller;

import com.example.todoapi.dto.TodoDto;
import com.example.todoapi.entity.Todo;
import com.example.todoapi.entity.User;
import com.example.todoapi.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Tag(name = "Todo", description = "할일 관리 API")
public class TodoController {
    private final TodoService todoService;

    @PostMapping
    @Operation(summary = "할일 생성", description = "새로운 할일을 생성합니다")
    public ResponseEntity<TodoDto.TodoResponse> createTodo(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody TodoDto.CreateRequest request) {
        TodoDto.TodoResponse response = todoService.createTodo(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "할일 목록 조회", description = "사용자의 모든 할일을 페이징하여 조회합니다")
    public ResponseEntity<Page<TodoDto.TodoResponse>> getAllTodos(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        Page<TodoDto.TodoResponse> todos = todoService.getAllTodos(user, pageable);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{todoId}")
    @Operation(summary = "할일 상세 조회", description = "특정 할일을 조회합니다")
    public ResponseEntity<TodoDto.TodoResponse> getTodoById(
            @AuthenticationPrincipal User user,
            @PathVariable Long todoId) {
        TodoDto.TodoResponse response = todoService.getTodoById(user, todoId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{todoId}")
    @Operation(summary = "할일 수정", description = "할일을 수정합니다")
    public ResponseEntity<TodoDto.TodoResponse> updateTodo(
            @AuthenticationPrincipal User user,
            @PathVariable Long todoId,
            @RequestBody TodoDto.UpdateRequest request) {
        TodoDto.TodoResponse response = todoService.updateTodo(user, todoId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{todoId}")
    @Operation(summary = "할일 삭제", description = "할일을 삭제합니다")
    public ResponseEntity<Void> deleteTodo(
            @AuthenticationPrincipal User user,
            @PathVariable Long todoId) {
        todoService.deleteTodo(user, todoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "할일 검색", description = "조건에 따라 할일을 검색합니다")
    public ResponseEntity<List<TodoDto.TodoResponse>> searchTodos(
            @AuthenticationPrincipal User user,
            @ModelAttribute TodoDto.SearchRequest searchRequest) {
        List<TodoDto.TodoResponse> todos = todoService.searchTodos(user, searchRequest);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/status/{completed}")
    @Operation(summary = "상태별 할일 조회", description = "완료/미완료 상태별로 할일을 조회합니다")
    public ResponseEntity<List<TodoDto.TodoResponse>> getTodosByStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Boolean completed) {
        List<TodoDto.TodoResponse> todos = todoService.getTodosByStatus(user, completed);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "우선순위별 할일 조회", description = "우선순위별로 할일을 조회합니다")
    public ResponseEntity<List<TodoDto.TodoResponse>> getTodosByPriority(
            @AuthenticationPrincipal User user,
            @PathVariable Todo.Priority priority) {
        List<TodoDto.TodoResponse> todos = todoService.getTodosByPriority(user, priority);
        return ResponseEntity.ok(todos);
    }
}