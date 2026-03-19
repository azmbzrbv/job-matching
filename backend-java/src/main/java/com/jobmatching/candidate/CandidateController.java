package com.jobmatching.candidate;

import com.jobmatching.candidate.dto.CandidateRequestDTO;
import com.jobmatching.candidate.dto.CandidateResponseDTO;
import com.jobmatching.job.dto.JobResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService){
        this.candidateService = candidateService;
    }

    @PostMapping("/register")
    public ResponseEntity<CandidateResponseDTO> register(@Valid @RequestBody CandidateRequestDTO dto) {
        CandidateResponseDTO saved = candidateService.registerCandidate(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponseDTO> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.fetchCandidateProfile(id));
    }

    @PostMapping("/{id}/cv-upload")
    public ResponseEntity<String> uploadCv(@PathVariable Long id, @RequestParam("file") MultipartFile file){
        candidateService.processAndSaveCv(id, file);
        return ResponseEntity.ok("CV uploaded successfully");
    }

    @GetMapping("/{id}/matches")
    public ResponseEntity<List<JobResponseDTO>> getMatchedJobs(@PathVariable Long id){
        return ResponseEntity.ok(candidateService.fetchMatchedJobs(id));
    }
}
