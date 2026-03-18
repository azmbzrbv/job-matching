package com.jobmatching.Job.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JobRequestDto(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title is too long")
        String title,

        @NotBlank(message = "Description is required")
        @Size(min = 50, message = "Description must be at least 50 characters for better matching")
        String description,

        @NotNull
        Long recruiter_id
) {
}
