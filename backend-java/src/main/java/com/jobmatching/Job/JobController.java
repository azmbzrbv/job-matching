package com.jobmatching.Job;


import com.jobmatching.Job.dto.JobRequestDto;
import com.jobmatching.Job.dto.JobResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // For Candidates: Browse all available jobs
    @GetMapping
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {
        return ResponseEntity.ok(jobService.fetchAllJobs());
    }

    // For Recruiters: Post a new job
    @PostMapping
    public ResponseEntity<JobResponseDTO> postJob(@Valid @RequestBody JobRequestDto jobRequestDto) {
        return ResponseEntity.ok(jobService.createJob(jobRequestDto));
    }

    //For Recruiters: get jobs that was posted by recruiter
    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<JobResponseDTO>> getMyJobs(@PathVariable Long recruiterId) {
        List<JobResponseDTO> jobs = jobService.fetchJobsByRecruiter(recruiterId);
        return ResponseEntity.ok(jobs);
    }


    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDTO> getJobDetails(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.fetchJobById(id));
    }


    // Example: GET /api/jobs/search?title=java
    @GetMapping("/search")
    public ResponseEntity<List<JobResponseDTO>> searchJobs(@RequestParam String title) {
        List<JobResponseDTO> results = jobService.fetchJobsByTitle(title);
        return ResponseEntity.ok(results);
    }

}
