package com.taskgenerator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for task generation response
 */
@Schema(description = "Response containing the generated structured task list")
public class TaskGenerationResponse {

    @Schema(description = "The original objective provided in the request", 
            example = "Develop a mobile food delivery app")
    private String originalObjective;
    
    @Schema(description = "List of generated tasks")
    private List<Task> tasks;
    
    @Schema(description = "Timestamp when the tasks were generated", 
            example = "2025-11-07T10:30:00")
    private LocalDateTime generatedAt;
    
    @Schema(description = "AI model used to generate the tasks", 
            example = "gpt-3.5-turbo")
    private String model;

    public TaskGenerationResponse() {
    }

    public TaskGenerationResponse(String originalObjective, List<Task> tasks, LocalDateTime generatedAt, String model) {
        this.originalObjective = originalObjective;
        this.tasks = tasks;
        this.generatedAt = generatedAt;
        this.model = model;
    }

    public String getOriginalObjective() {
        return originalObjective;
    }

    public void setOriginalObjective(String originalObjective) {
        this.originalObjective = originalObjective;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String originalObjective;
        private List<Task> tasks = new ArrayList<>();
        private LocalDateTime generatedAt;
        private String model;

        public Builder originalObjective(String originalObjective) {
            this.originalObjective = originalObjective;
            return this;
        }

        public Builder tasks(List<Task> tasks) {
            this.tasks = tasks;
            return this;
        }

        public Builder generatedAt(LocalDateTime generatedAt) {
            this.generatedAt = generatedAt;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public TaskGenerationResponse build() {
            return new TaskGenerationResponse(originalObjective, tasks, generatedAt, model);
        }
    }

    @Schema(description = "Individual task with details")
    public static class Task {
        @Schema(description = "Task order/sequence number", 
                example = "1")
        private Integer order;
        
        @Schema(description = "Task title", 
                example = "Define project requirements and scope")
        private String title;
        
        @Schema(description = "Detailed description of the task", 
                example = "Gather stakeholders and define essential features...")
        private String description;
        
        @Schema(description = "Task priority level", 
                example = "high",
                allowableValues = {"high", "medium", "low"})
        private String priority;
        
        @Schema(description = "Estimated hours to complete the task", 
                example = "16")
        private Integer estimatedHours;
        
        @Schema(description = "List of task IDs that this task depends on", 
                example = "[\"1\", \"2\"]")
        private List<String> dependencies;

        public Task() {
        }

        public Task(Integer order, String title, String description, String priority, Integer estimatedHours, List<String> dependencies) {
            this.order = order;
            this.title = title;
            this.description = description;
            this.priority = priority;
            this.estimatedHours = estimatedHours;
            this.dependencies = dependencies;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public Integer getEstimatedHours() {
            return estimatedHours;
        }

        public void setEstimatedHours(Integer estimatedHours) {
            this.estimatedHours = estimatedHours;
        }

        public List<String> getDependencies() {
            return dependencies;
        }

        public void setDependencies(List<String> dependencies) {
            this.dependencies = dependencies;
        }

        public static TaskBuilder builder() {
            return new TaskBuilder();
        }

        public static class TaskBuilder {
            private Integer order;
            private String title;
            private String description;
            private String priority;
            private Integer estimatedHours;
            private List<String> dependencies = new ArrayList<>();

            public TaskBuilder order(Integer order) {
                this.order = order;
                return this;
            }

            public TaskBuilder title(String title) {
                this.title = title;
                return this;
            }

            public TaskBuilder description(String description) {
                this.description = description;
                return this;
            }

            public TaskBuilder priority(String priority) {
                this.priority = priority;
                return this;
            }

            public TaskBuilder estimatedHours(Integer estimatedHours) {
                this.estimatedHours = estimatedHours;
                return this;
            }

            public TaskBuilder dependencies(List<String> dependencies) {
                this.dependencies = dependencies;
                return this;
            }

            public Task build() {
                return new Task(order, title, description, priority, estimatedHours, dependencies);
            }
        }
    }
}
