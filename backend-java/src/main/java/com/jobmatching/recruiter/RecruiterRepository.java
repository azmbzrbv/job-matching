package com.jobmatching.recruiter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    Optional<Recruiter> findByUserEmail(String email);
    boolean existsByUserEmail(String email);
}
