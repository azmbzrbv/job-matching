package com.jobmatching.Application.dto;

import com.jobmatching.Application.Application;
import com.jobmatching.Application.ApplicationStatus;

import java.time.LocalDateTime;

public record ApplicationResponseDTO(
        Long id,
        Long candidateId,
        String candidateName,
        Long jobId,
        String jobTitle,
        Double matchScore,
        ApplicationStatus status,
        LocalDateTime appliedAt
) {
    public ApplicationResponseDTO(Application app) {
        this(
                app.getId(),
                app.getCandidate().getId(),
                app.getCandidate().getFullName(),
                app.getJob().getId(),
                app.getJob().getTitle(),
                app.getMatchScore(),
                app.getStatus(),
                app.getAppliedAt()
        );
    }
}
