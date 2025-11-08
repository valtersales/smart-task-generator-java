package com.taskgenerator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskgenerator.dto.TaskGenerationRequest;
import com.taskgenerator.dto.TaskGenerationResponse;
import com.taskgenerator.service.TaskGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskGeneratorController.class)
class TaskGeneratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskGeneratorService taskGeneratorService;

    @Test
    void healthCheck_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Smart Task Generator is running!"));
    }

    @Test
    void generateTasks_WithValidObjective_ShouldReturnTaskList() throws Exception {
        // Arrange
        TaskGenerationRequest request = new TaskGenerationRequest();
        request.setObjective("Develop an e-commerce website");
        request.setMaxTasks(5);
        request.setDetailLevel("medium");

        TaskGenerationResponse.Task task = TaskGenerationResponse.Task.builder()
                .order(1)
                .title("Plan architecture")
                .description("Define technologies and structure")
                .priority("high")
                .estimatedHours(8)
                .dependencies(List.of())
                .build();

        TaskGenerationResponse response = TaskGenerationResponse.builder()
                .originalObjective(request.getObjective())
                .tasks(List.of(task))
                .generatedAt(LocalDateTime.now())
                .model("gpt-3.5-turbo")
                .build();

        when(taskGeneratorService.generateTasks(any(TaskGenerationRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/tasks/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalObjective").value(request.getObjective()))
                .andExpect(jsonPath("$.tasks").isArray())
                .andExpect(jsonPath("$.tasks[0].title").value("Plan architecture"));
    }

    @Test
    void generateTasks_WithEmptyObjective_ShouldReturnBadRequest() throws Exception {
        // Arrange
        TaskGenerationRequest request = new TaskGenerationRequest();
        request.setObjective("");
        request.setMaxTasks(5);

        // Act & Assert
        mockMvc.perform(post("/api/v1/tasks/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

