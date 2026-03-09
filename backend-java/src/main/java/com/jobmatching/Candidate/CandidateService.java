package com.jobmatching.Candidate;


import com.jobmatching.Job.Job;
import com.jobmatching.Job.JobService;
import com.jobmatching.Job.dto.JobResponseDTO;
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


    public void processAndSaveCv(Long id, MultipartFile file){
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Candidate Not Found"));

        // we need to use weblient here
        String extractedText = mlClient.extractTextFromPDF(file);

        // 3. Update the candidate entity
        candidate.setResumeText(extractedText);
        candidateRepository.save(candidate);
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
