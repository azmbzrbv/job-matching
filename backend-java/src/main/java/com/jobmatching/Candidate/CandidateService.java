package com.jobmatching.Candidate;


import com.jobmatching.Candidate.dto.CandidateRequestDTO;
import com.jobmatching.Candidate.dto.CandidateResponseDTO;
import com.jobmatching.Job.Job;
import com.jobmatching.Job.JobService;
import com.jobmatching.Job.dto.JobResponseDTO;
import com.jobmatching.exception.BadRequestException;
import com.jobmatching.exception.ResourceNotFoundException;
import com.jobmatching.mlservice.MLClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class CandidateService {

    private final JobService jobService;
    private final CandidateRepository candidateRepository;
    private final MLClient mlClient;

    public CandidateService(CandidateRepository candidateRepository, MLClient mlClient, JobService jobService){
        this.candidateRepository = candidateRepository;
        this.mlClient = mlClient;
        this.jobService = jobService;
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

    public void processAndSaveCv(Long id, MultipartFile file) {
        if (file.isEmpty() || !file.getContentType().equals("application/pdf")) {
            throw new BadRequestException("Please upload a valid PDF file");
        }

        Candidate candidate = getCandidateById(id);

        try {
            String extractedText = mlClient.extractTextFromPDF(file);
            candidate.setResumeText(extractedText);
            candidateRepository.save(candidate);
        } catch (Exception e) {
            throw new RuntimeException("ML Service Error: Could not parse CV", e);
        }
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
