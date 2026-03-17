package com.jobmatching.Job;


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
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.returnAllJobs());
    }

    // For Recruiters: Post a new job
    @PostMapping
    public ResponseEntity<Job> postJob(@Valid @RequestBody Job job) {
        return ResponseEntity.ok(jobService.createJob(job));
    }

    //For Recruiters: get jobs that was posted by recruiter
    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<Job>> getMyJobs(@PathVariable Long recruiterId) {
        List<Job> jobs = jobService.getJobsByRecruiter(recruiterId);
        return ResponseEntity.ok(jobs);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobDetails(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.findJobById(id));
    }


    // Example: GET /api/jobs/search?title=java
    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(@RequestParam String title) {
        List<Job> results = jobService.searchJobsByTitle(title);
        return ResponseEntity.ok(results);
    }

}
