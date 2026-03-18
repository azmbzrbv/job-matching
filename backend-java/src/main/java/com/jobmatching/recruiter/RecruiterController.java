package com.jobmatching.recruiter;


import com.jobmatching.recruiter.dto.RecruiterRequestDTO;
import com.jobmatching.recruiter.dto.RecruiterResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/recruiters")
public class RecruiterController {

    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @PostMapping("/register")
    public ResponseEntity<RecruiterResponseDTO> register(@Valid @RequestBody RecruiterRequestDTO dto) {
        RecruiterResponseDTO saved = recruiterService.registerRecruiter(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruiterResponseDTO> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(recruiterService.fetchRecruiterProfile(id));
    }
}
