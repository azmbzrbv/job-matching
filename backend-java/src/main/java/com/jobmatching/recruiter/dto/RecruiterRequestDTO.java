package com.jobmatching.recruiter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecruiterRequestDTO(
        @NotBlank(message = "Recruiter name is required")
        String name,

        @Email(message = "Please provide a valid email address")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Company name is required")
        String companyName
) {
}
