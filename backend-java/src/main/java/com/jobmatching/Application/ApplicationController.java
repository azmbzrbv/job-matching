package com.jobmatching.Application;


import com.jobmatching.Candidate.Candidate;
import com.jobmatching.Job.Job;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/applications")
public class ApplicationController {

    private ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService){
        this.applicationService = applicationService;
    }


    @GetMapping
    public List<Application> findAllApplications() {
        return applicationService.getAllApplications(); 
    }

    @PostMapping("/apply")
    public ResponseEntity<Application> apply(@RequestParam Long candidateId, @RequestParam Long jobId){
        Application newApplication = applicationService.createApplication(candidateId, jobId);
        return ResponseEntity.ok(newApplication);
    }

    // Recruiter View: Ranked Applicants
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getRankedApplicants(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsByJob(jobId));
    }

    // Candidate View: My Applications
    @GetMapping("/my-applications/{candidateId}")
    public ResponseEntity<List<Application>> getMyApplications(@PathVariable Long candidateId) {
        return ResponseEntity.ok(applicationService.getApplicationsByCandidate(candidateId));
    }

    // Recruiter Action: Update Status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Application> updateStatus(@PathVariable Long id, @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.updateStatus(id, status));
    }
}
