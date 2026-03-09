package com.jobmatching.Candidate;

import com.jobmatching.Job.dto.JobResponseDTO;
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


    //cv logic
    @PostMapping("{id}/cv-upload")
    public ResponseEntity<String> uploadCv(@PathVariable Long id, @RequestParam("file") MultipartFile file){
        candidateService.processAndSaveCv(id, file);
        return ResponseEntity.ok("CV uploaded succesfully");
    }

    @GetMapping("{id}/matches")
    public List<JobResponseDTO> getMatchedJobs(@PathVariable Long id){
        return candidateService.returnMatchedJobs(id);
    }

}
