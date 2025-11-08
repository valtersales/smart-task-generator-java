package com.taskgenerator.controller;

import com.taskgenerator.dto.TaskGenerationRequest;
import com.taskgenerator.dto.TaskGenerationResponse;
import com.taskgenerator.service.TaskGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for task generation
 */
@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task Generator", description = "API for intelligent task generation")
public class TaskGeneratorController {

    private final TaskGeneratorService taskGeneratorService;

    public TaskGeneratorController(TaskGeneratorService taskGeneratorService) {
        this.taskGeneratorService = taskGeneratorService;
    }

    @PostMapping("/generate")
    @Operation(summary = "Generate tasks", description = "Generates a structured task list from an objective")
    public ResponseEntity<TaskGenerationResponse> generateTasks(
            @Valid @RequestBody TaskGenerationRequest request) {

        TaskGenerationResponse response = taskGeneratorService.generateTasks(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Checks if the service is running")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Smart Task Generator is running!");
    }
}
