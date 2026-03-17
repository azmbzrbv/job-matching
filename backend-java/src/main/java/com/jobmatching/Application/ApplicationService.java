package com.jobmatching.Application;


import com.jobmatching.Candidate.Candidate;
import com.jobmatching.Candidate.CandidateService;
import com.jobmatching.Job.Job;
import com.jobmatching.Job.JobService;
import com.jobmatching.exception.BadRequestException;
import com.jobmatching.exception.ResourceNotFoundException;
import com.jobmatching.mlservice.MLClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final MLClient mlClient;
    private final JobService jobService;
    private final CandidateService candidateService;


    public ApplicationService(ApplicationRepository applicationRepository,
                                MLClient mlClient,
                                JobService jobService,
                                CandidateService candidateService) {
        this.applicationRepository = applicationRepository;
        this.mlClient = mlClient;
        this.jobService = jobService;
        this.candidateService = candidateService;
    }


    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public List<Application> getApplicationsByCandidate(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId);
    }

    public Application createApplication(Long candidateId, Long jobId) {
        // Duplicate Check
        if (applicationRepository.existsByCandidateIdAndJobId(candidateId, jobId)) {
            throw new BadRequestException("You have already applied for this position.");
        }

        Candidate candidate = candidateService.findCandidateById(candidateId);
        Job job = jobService.findJobById(jobId);

        // Score Calculation
        Map<Long, Double> result = mlClient.rankCandidates(job.getDescription(), List.of(candidate));
        Double score = result.getOrDefault(candidateId, 0.0);

        // Create Entity
        Application app = new Application();
        app.setJob(job);
        app.setCandidate(candidate);
        app.setMatchScore(score);
        app.setStatus(ApplicationStatus.PENDING); // Set default here

        return applicationRepository.save(app);
    }

    // Update application status (For Recruiters)
    public Application updateStatus(Long applicationId, ApplicationStatus status) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        app.setStatus(status);
        return applicationRepository.save(app);
    }

    public List<Application> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJobIdOrderByMatchScoreDesc(jobId);
    }
}
