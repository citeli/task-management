package com.taskmanagement.dto;

import com.taskmanagement.domain.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskRequestDTO {
    @NotBlank(message = "Title must not be empty")
    public String title;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    public String description;

    public Status status;
}
