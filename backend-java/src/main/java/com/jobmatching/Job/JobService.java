package com.jobmatching.Job;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> returnAllJobs() {
        return jobRepository.findAll();
    }

    public Job findJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }

    public Job createJob(Job job) {
        //TODO: validation
        return jobRepository.save(job);
    }

    public List<Job> getJobsByRecruiter(Long recruiterId) {
        return jobRepository.findByRecruiterId(recruiterId);
    }

    public List<Job> searchJobsByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return jobRepository.findAll(); // If search is empty, show everything
        }
        return jobRepository.findByTitleContainingIgnoreCase(title);
    }
}
