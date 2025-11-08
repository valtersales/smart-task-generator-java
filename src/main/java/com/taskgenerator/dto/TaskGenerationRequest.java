package com.taskgenerator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for task generation request
 */
@Schema(description = "Request for generating a structured task list from an objective")
public class TaskGenerationRequest {

    @Schema(description = "The main objective to be broken down into tasks", example = "Develop a mobile food delivery app", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "The objective cannot be empty")
    @Size(min = 10, max = 1000, message = "The objective must be between 10 and 1000 characters")
    private String objective;

    @Schema(description = "Maximum number of tasks to generate", example = "8", defaultValue = "10")
    private Integer maxTasks = 10;

    @Schema(description = "Level of detail for each task", example = "medium", allowableValues = { "low", "medium",
            "high" }, defaultValue = "medium")
    private String detailLevel = "medium"; // low, medium, high

    public TaskGenerationRequest() {
    }

    public TaskGenerationRequest(String objective, Integer maxTasks, String detailLevel) {
        this.objective = objective;
        this.maxTasks = maxTasks;
        this.detailLevel = detailLevel;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public Integer getMaxTasks() {
        return maxTasks;
    }

    public void setMaxTasks(Integer maxTasks) {
        this.maxTasks = maxTasks;
    }

    public String getDetailLevel() {
        return detailLevel;
    }

    public void setDetailLevel(String detailLevel) {
        this.detailLevel = detailLevel;
    }
}
