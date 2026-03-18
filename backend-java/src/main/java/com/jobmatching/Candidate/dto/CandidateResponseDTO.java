package com.jobmatching.Candidate.dto;

import com.jobmatching.Candidate.Candidate;

public record CandidateResponseDTO(
        Long id,
        String fullName,
        String email
) {
    public CandidateResponseDTO(Candidate candidate){
        this(
                candidate.getId(),
                candidate.getFullName(),
                candidate.getEmail()
        );
    }
}
