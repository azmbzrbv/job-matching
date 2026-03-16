package com.jobmatching.Candidate;


import com.jobmatching.Job.Job;
import com.jobmatching.Job.JobService;
import com.jobmatching.Job.dto.JobResponseDTO;
import com.jobmatching.mlservice.MLClient;
import org.springframework.http.ResponseEntity;
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


    public Candidate findCandidateById(Long id){
        return candidateRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Candidate with id: "+id+" not found"));
    }

    public Candidate registerCandidate(String fullName, String email) {
        if (candidateRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }
        Candidate candidate = new Candidate();
        candidate.setFullName(fullName);
        candidate.setEmail(email);
        return candidateRepository.save(candidate);
    }

    public ResponseEntity<Candidate> getCandidateProfile(Long id){
        return candidateRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public void processAndSaveCv(Long id, MultipartFile file) {
        // 1. Basic File Validation
        if (file.isEmpty() || !file.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Please upload a valid PDF file");
        }

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate Not Found"));

        // 2. ML Extraction
        try {
            String extractedText = mlClient.extractTextFromPDF(file);
            candidate.setResumeText(extractedText);
            candidateRepository.save(candidate);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process PDF: " + e.getMessage());
        }
    }


    public List<JobResponseDTO> returnMatchedJobs(Long id){
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Candidate Not Found"));

        String resumeText = candidate.getResumeText();

        if (resumeText == null || resumeText.isEmpty()) {
            throw new RuntimeException("Please upload your CV first before fetching matches.");
        }

        List<Job> candidateJobs = jobService.returnAllJobs();

        Map<Long, Double> scores = mlClient.rankJobs(resumeText, candidateJobs);

        // 5. Sort and Map to DTOs
        return candidateJobs.stream()
                .map(job -> new JobResponseDTO(job, scores.get(job.getId())))
                .sorted(Comparator.comparing(JobResponseDTO::matchScore).reversed())
                .limit(10) // Only show top 10
                .toList();
    }


}
