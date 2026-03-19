package com.jobmatching.application;


import com.jobmatching.candidate.Candidate;
import com.jobmatching.job.Job;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;


    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    private double matchScore;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime appliedAt;

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }
}
