package com.jobmatching.Candidate;


import com.jobmatching.mlservice.MLClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final MLClient mlClient;

    public CandidateService(CandidateRepository candidateRepository, MLClient mlClient){
        this.candidateRepository = candidateRepository;
        this.mlClient = mlClient;
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
}
