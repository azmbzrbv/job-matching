package com.jobmatching.candidate.dto;

import com.jobmatching.candidate.Candidate;

public record CandidateResponseDTO(
        Long id,
        String fullName,
        String email
) {
    public CandidateResponseDTO(Candidate candidate){
        this(
                candidate.getId(),
                candidate.getFullName(),
                candidate.getUser()!=null ? candidate.getUser().getEmail() : null
        );
    }
}
