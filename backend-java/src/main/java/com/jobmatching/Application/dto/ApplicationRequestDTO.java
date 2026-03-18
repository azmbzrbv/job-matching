package com.jobmatching.Application.dto;

import jakarta.validation.constraints.NotNull;

public record ApplicationRequestDTO(
        @NotNull(message = "Job ID is required")
        Long jobId,

        @NotNull(message = "Candidate ID is required")
        Long candidateId // We will get it later from security
) {}
