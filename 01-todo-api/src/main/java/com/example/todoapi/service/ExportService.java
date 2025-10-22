package com.example.todoapi.service;

import com.example.todoapi.entity.Todo;
import com.example.todoapi.entity.User;
import com.example.todoapi.repository.TodoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExportService {
    private final TodoRepository todoRepository;
    private final ObjectMapper objectMapper;

    public byte[] exportTodosAsJson(User user) throws Exception {
        List<Todo> todos = todoRepository.findByUser(user, null).getContent();
        return objectMapper.writeValueAsBytes(todos);
    }

    public byte[] exportTodosAsCsv(User user) throws Exception {
        List<Todo> todos = todoRepository.findByUser(user, null).getContent();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(out);

        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withHeader("ID", "Title", "Description", "Status", "Priority", "Due Date", "Category", "Created At", "Updated At");

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
            for (Todo todo : todos) {
                csvPrinter.printRecord(
                        todo.getId(),
                        todo.getTitle(),
                        todo.getDescription(),
                        todo.getCompleted() ? "Completed" : "Pending",
                        todo.getPriority().toString(),
                        todo.getDueDate() != null ? todo.getDueDate().toString() : "",
                        todo.getCategory() != null ? todo.getCategory().getName() : "",
                        todo.getCreatedAt().toString(),
                        todo.getUpdatedAt().toString()
                );
            }
            csvPrinter.flush();
        }

        return out.toByteArray();
    }
}