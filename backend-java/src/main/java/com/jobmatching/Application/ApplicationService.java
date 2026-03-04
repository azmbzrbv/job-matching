package com.jobmatching.Application;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;


    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    //later matching logic should be added here
    public Application submitApplication(Application application) {
        // Default status for new applications
        if (application.getStatus() == null) {
            application.setStatus("PENDING");
        }
        return applicationRepository.save(application);
    }


    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }


    public List<Application> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    public List<Application> getApplicationsByCandidate(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId);
    }
}
