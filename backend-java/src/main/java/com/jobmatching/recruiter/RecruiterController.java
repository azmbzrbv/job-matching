package com.jobmatching.recruiter;


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
    public ResponseEntity<Recruiter> register(@Valid @RequestBody Recruiter recruiter) {
        Recruiter savedRecruiter = recruiterService.registerRecruiter(recruiter);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecruiter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recruiter> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(recruiterService.getRecruiterById(id));
    }
}
