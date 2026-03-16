package com.jobmatching.Application;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    // For sorting the recruiter view by score
    List<Application> findByJobIdOrderByMatchScoreDesc(Long jobId);

    List<Application> findByCandidateId(Long candidateId);

    // To check if someone already applied
    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);
}
