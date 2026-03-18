package com.jobmatching.Candidate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CandidateRequestDTO(
        @NotBlank(message = "Full name is required")
        String fullName,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email
) { }
