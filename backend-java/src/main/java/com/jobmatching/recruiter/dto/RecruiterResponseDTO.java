package com.jobmatching.recruiter.dto;

import com.jobmatching.recruiter.Recruiter;

public record RecruiterResponseDTO(
        Long id,
        String name,
        String email,
        String companyName,
        int jobsCount
) {
    public RecruiterResponseDTO(Recruiter recruiter){
        this(
             recruiter.getId(),
             recruiter.getName(),
             recruiter.getEmail(),
             recruiter.getCompanyName(),
             recruiter.getJobs().size()
        );
    }
}
