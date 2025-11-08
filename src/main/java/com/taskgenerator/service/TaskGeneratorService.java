package com.taskgenerator.service;

import com.taskgenerator.dto.TaskGenerationRequest;
import com.taskgenerator.dto.TaskGenerationResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service responsible for generating tasks using LLM
 */
@Service
public class TaskGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(TaskGeneratorService.class);

    private final ChatClient chatClient;
    private final String aiProviderInfo;

    @Value("${app.ai.model-name:gpt-3.5-turbo}")
    private String modelName;

    public TaskGeneratorService(ChatClient.Builder chatClientBuilder, String aiProviderInfo) {
        this.chatClient = chatClientBuilder.build();
        this.aiProviderInfo = aiProviderInfo;
        logger.info("🤖 TaskGeneratorService started with: {}", aiProviderInfo);
    }

    /**
     * Generates a structured task list from an objective
     */
    public TaskGenerationResponse generateTasks(TaskGenerationRequest request) {
        logger.info("Generating tasks for objective: {}", request.getObjective());

        String promptText = buildPrompt(request);

        String response = chatClient.prompt()
                .user(promptText)
                .call()
                .content();

        logger.debug("LLM Response: {}", response);

        List<TaskGenerationResponse.Task> tasks = parseResponse(response);

        return TaskGenerationResponse.builder()
                .originalObjective(request.getObjective())
                .tasks(tasks)
                .generatedAt(LocalDateTime.now())
                .model(aiProviderInfo)
                .build();
    }

    /**
     * Builds the prompt for the LLM
     */
    private String buildPrompt(TaskGenerationRequest request) {
        String templateText = """
                You are an assistant specialized in project planning and organization.

                Provided objective: {objective}

                Please break down this objective into a structured task list.

                Rules:
                - Generate a maximum of {maxTasks} tasks
                - Detail level: {detailLevel}
                - Each task must have: order, title, description, priority (high/medium/low), estimated hours
                - Identify dependencies between tasks when applicable

                Expected response format (use exactly this format):

                TASK 1:
                Title: [task title]
                Description: [detailed description]
                Priority: [high/medium/low]
                Estimate: [number] hours
                Dependencies: [list of task numbers or "none"]

                TASK 2:
                ...

                Be specific, practical and organize the tasks logically.
                """;

        PromptTemplate promptTemplate = new PromptTemplate(templateText);
        Map<String, Object> model = Map.of(
                "objective", request.getObjective(),
                "maxTasks", request.getMaxTasks().toString(),
                "detailLevel", request.getDetailLevel());
        Prompt prompt = promptTemplate.create(model);

        return prompt.getContents();
    }

    /**
     * Parses the LLM response and extracts structured tasks
     */
    private List<TaskGenerationResponse.Task> parseResponse(String response) {
        List<TaskGenerationResponse.Task> tasks = new ArrayList<>();

        // Pattern to identify each task
        Pattern taskPattern = Pattern.compile(
                "TASK\\s+(\\d+):\\s*\\n" +
                        "Title:\\s*(.+?)\\n" +
                        "Description:\\s*(.+?)\\n" +
                        "Priority:\\s*(.+?)\\n" +
                        "Estimate:\\s*(\\d+)\\s*hours?\\n" +
                        "Dependencies:\\s*(.+?)(?=\\n\\nTASK|\\n*$)",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = taskPattern.matcher(response);

        while (matcher.find()) {
            Integer order = Integer.parseInt(matcher.group(1));
            String title = matcher.group(2).trim();
            String description = matcher.group(3).trim();
            String priority = matcher.group(4).trim().toLowerCase();
            Integer estimate = Integer.parseInt(matcher.group(5));
            String dependenciesStr = matcher.group(6).trim();

            List<String> dependencies = parseDependencies(dependenciesStr);

            TaskGenerationResponse.Task task = TaskGenerationResponse.Task.builder()
                    .order(order)
                    .title(title)
                    .description(description)
                    .priority(priority)
                    .estimatedHours(estimate)
                    .dependencies(dependencies)
                    .build();

            tasks.add(task);
        }

        // If parsing fails, create at least one task with the complete response
        if (tasks.isEmpty()) {
            logger.warn("Could not parse structured response. Returning raw response.");
            tasks.add(TaskGenerationResponse.Task.builder()
                    .order(1)
                    .title("Generated Tasks")
                    .description(response)
                    .priority("medium")
                    .estimatedHours(0)
                    .dependencies(List.of())
                    .build());
        }

        return tasks;
    }

    /**
     * Parses the dependencies of a task
     */
    private List<String> parseDependencies(String dependenciesStr) {
        if (dependenciesStr == null ||
                dependenciesStr.equalsIgnoreCase("nenhuma") ||
                dependenciesStr.equalsIgnoreCase("none") ||
                dependenciesStr.trim().isEmpty()) {
            return List.of();
        }

        return Arrays.stream(dependenciesStr.split("[,;]"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
