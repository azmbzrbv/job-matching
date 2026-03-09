package com.jobmatching.Job.dto;

import com.jobmatching.Job.Job;

public record JobResponseDTO(
         Long id,
         String title,
         String description,
         Double matchScore) {

    public JobResponseDTO(Job job, Double matchScore){
        this(job.getId(), job.getTitle(), job.getDescription(), matchScore);
    }
}
