package com.jobmatching.recruiter;

import com.jobmatching.exception.BadRequestException;
import com.jobmatching.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RecruiterService {

    private final RecruiterRepository recruiterRepository;

    public RecruiterService(RecruiterRepository recruiterRepository) {
        this.recruiterRepository = recruiterRepository;
    }

    public Recruiter registerRecruiter(Recruiter recruiter) {
        if (recruiterRepository.existsByEmail(recruiter.getEmail())) {
            throw new BadRequestException("Email already in use by another recruiter");
        }
        return recruiterRepository.save(recruiter);
    }

    public Recruiter getRecruiterById(Long id) {
        return recruiterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
    }
}
