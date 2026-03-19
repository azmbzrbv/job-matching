package com.jobmatching.job.dto;

import com.jobmatching.job.Job;

public record JobResponseDTO(
         Long id,
         String title,
         String description,
         Double matchScore) {

    public JobResponseDTO(Job job, Double matchScore){
        this(job.getId(), job.getTitle(), job.getDescription(), matchScore);
    }
}
