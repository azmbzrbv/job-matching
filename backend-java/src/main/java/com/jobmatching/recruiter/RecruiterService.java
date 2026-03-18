package com.jobmatching.recruiter;

import com.jobmatching.exception.BadRequestException;
import com.jobmatching.exception.ResourceNotFoundException;
import com.jobmatching.recruiter.dto.RecruiterRequestDTO;
import com.jobmatching.recruiter.dto.RecruiterResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class RecruiterService {

    private final RecruiterRepository recruiterRepository;

    public RecruiterService(RecruiterRepository recruiterRepository) {
        this.recruiterRepository = recruiterRepository;
    }

    // --- INTERNAL GETTER (For other services like JobService) ---
    public Recruiter getRecruiterById(Long id) {
        return recruiterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found with id: " + id));
    }

    // --- EXTERNAL FETCHERS (For Controller) ---
    public RecruiterResponseDTO fetchRecruiterProfile(Long id) {
        Recruiter recruiter = getRecruiterById(id);
        return new RecruiterResponseDTO(recruiter);
    }

    public RecruiterResponseDTO registerRecruiter(RecruiterRequestDTO dto) {
        if (recruiterRepository.existsByEmail(dto.email())) {
            throw new BadRequestException("Email already in use by another recruiter");
        }

        Recruiter recruiter = new Recruiter();
        recruiter.setName(dto.name());
        recruiter.setEmail(dto.email());
        recruiter.setCompanyName(dto.companyName());
        recruiter.setId(null);

        Recruiter saved = recruiterRepository.save(recruiter);
        return new RecruiterResponseDTO(saved);
    }
}
