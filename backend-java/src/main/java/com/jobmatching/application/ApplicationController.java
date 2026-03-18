package com.jobmatching.application;


import com.jobmatching.application.dto.ApplicationRequestDTO;
import com.jobmatching.application.dto.ApplicationResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<ApplicationResponseDTO>> findAllApplications() {
        return ResponseEntity.ok(applicationService.fetchAllApplications());
    }

    @PostMapping("/apply")
    public ResponseEntity<ApplicationResponseDTO> apply(@Valid @RequestBody ApplicationRequestDTO applicationRequestDTO) {
        ApplicationResponseDTO response = applicationService.createApplication(applicationRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Recruiter View: Ranked Applicants
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getRankedApplicants(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.fetchApplicationsByJob(jobId));
    }

    // Candidate View: My Applications
    @GetMapping("/my-applications/{candidateId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getMyApplications(@PathVariable Long candidateId) {
        return ResponseEntity.ok(applicationService.fetchApplicationsByCandidate(candidateId));
    }

    // Recruiter Action: Update Status
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponseDTO> updateStatus(@PathVariable Long id, @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.updateStatus(id, status));
    }
}
