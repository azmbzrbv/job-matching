package com.jobmatching.application;


import com.jobmatching.application.dto.ApplicationRequestDTO;
import com.jobmatching.application.dto.ApplicationResponseDTO;
import com.jobmatching.candidate.Candidate;
import com.jobmatching.candidate.CandidateService;
import com.jobmatching.job.Job;
import com.jobmatching.job.JobService;
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

    //get prefix for the entities(other services will use it)
    //fetch prefix for the dto(controller will use it)

    // --- INTERNAL GETTER (For other services like JobService) ---
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));
    }

    // --- EXTERNAL FETCHERS (For Controller) ---
    public List<ApplicationResponseDTO> fetchAllApplications() {
        return applicationRepository.findAll().stream()
                .map(application -> new ApplicationResponseDTO(application))
                .toList();
    }

    public List<ApplicationResponseDTO> fetchApplicationsByCandidate(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId).stream()
                .map(application -> new ApplicationResponseDTO(application))
                .toList();
    }

    public ApplicationResponseDTO createApplication(ApplicationRequestDTO applicationRequestDTO) {
        Long candidateId = applicationRequestDTO.candidateId();
        Long jobId = applicationRequestDTO.jobId();

        if (applicationRepository.existsByCandidateIdAndJobId(candidateId, jobId)) {
            throw new BadRequestException("You have already applied for this position.");
        }

        Candidate candidate = candidateService.getCandidateById(candidateId);
        Job job = jobService.getJobById(jobId);

        // Score Calculation
        Map<Long, Double> result = mlClient.rankCandidates(job.getDescription(), List.of(candidate));
        Double score = result.getOrDefault(candidateId, 0.0);

        // Create Entity
        Application app = new Application();
        app.setJob(job);
        app.setCandidate(candidate);
        app.setMatchScore(score);
        app.setStatus(ApplicationStatus.PENDING);

        Application saved = applicationRepository.save(app);
        return new ApplicationResponseDTO(saved);
    }

    // Update application status (For Recruiters)
    public ApplicationResponseDTO updateStatus(Long applicationId, ApplicationStatus status) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        app.setStatus(status);
        return new ApplicationResponseDTO(app);
    }

    public List<ApplicationResponseDTO> fetchApplicationsByJob(Long jobId) {
        return applicationRepository.findByJobIdOrderByMatchScoreDesc(jobId).stream()
                .map(application -> new ApplicationResponseDTO(application))
                .toList();
    }
}
