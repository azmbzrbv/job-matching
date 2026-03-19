package com.jobmatching.candidate;


import com.jobmatching.candidate.dto.CandidateRequestDTO;
import com.jobmatching.candidate.dto.CandidateResponseDTO;
import com.jobmatching.job.Job;
import com.jobmatching.job.JobService;
import com.jobmatching.job.dto.JobResponseDTO;
import com.jobmatching.exception.BadRequestException;
import com.jobmatching.exception.ResourceNotFoundException;
import com.jobmatching.mlservice.MLClient;
import com.jobmatching.storage.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class CandidateService {

    private final JobService jobService;
    private final CandidateRepository candidateRepository;
    private final MLClient mlClient;
    private final StorageService storageService;

    public CandidateService(CandidateRepository candidateRepository, MLClient mlClient, JobService jobService, StorageService storageService){
        this.candidateRepository = candidateRepository;
        this.mlClient = mlClient;
        this.jobService = jobService;
        this.storageService = storageService;
    }

    // --- INTERNAL GETTERS (For other services/internal logic) ---

    public Candidate getCandidateById(Long id){
        return candidateRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Candidate with id: " + id + " not found"));
    }

    // --- EXTERNAL FETCHERS (For Controller) ---

    public CandidateResponseDTO fetchCandidateProfile(Long id){
        Candidate candidate = getCandidateById(id);
        return new CandidateResponseDTO(candidate);
    }

    public CandidateResponseDTO registerCandidate(CandidateRequestDTO dto) {
        if (candidateRepository.existsByEmail(dto.email())) {
            throw new BadRequestException("Email already registered");
        }

        Candidate candidate = new Candidate();
        candidate.setFullName(dto.fullName());
        candidate.setEmail(dto.email());
        candidate.setId(null);

        Candidate saved = candidateRepository.save(candidate);
        return new CandidateResponseDTO(saved);
    }

    @Transactional // Ensures DB integrity
    public void processAndSaveCv(Long id, MultipartFile file) {
        Candidate candidate = getCandidateById(id);
        String extractedText;
        try {
            extractedText = mlClient.extractTextFromPDF(file);
        } catch (Exception e) {
            throw new RuntimeException("ML Service Error: Could not parse CV", e);
        }

        String fileName = storageService.store(file);
        candidate.setCvFilePath(fileName);
        candidate.setResumeText(extractedText);
        candidateRepository.save(candidate);
    }

    public List<JobResponseDTO> fetchMatchedJobs(Long id){
        Candidate candidate = getCandidateById(id);
        String resumeText = candidate.getResumeText();

        if (resumeText == null || resumeText.isEmpty()) {
            throw new BadRequestException("Please upload your CV first before fetching matches.");
        }

        List<Job> allJobs = jobService.getAllJobs();

        Map<Long, Double> scores = mlClient.rankJobs(resumeText, allJobs);

        return allJobs.stream()
                .map(job -> new JobResponseDTO(job, scores.getOrDefault(job.getId(), 0.0)))
                .sorted(Comparator.comparing(JobResponseDTO::matchScore).reversed())
                .limit(10)
                .toList();
    }
}
