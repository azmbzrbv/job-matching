package com.jobmatching.job;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jobmatching.application.Application;
import com.jobmatching.recruiter.Recruiter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title is too long")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 50, message = "Description must be at least 50 characters for better matching")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "A job must belong to a recruiter")
    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;

    @JsonIgnore
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Application> applications = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Recruiter getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(Recruiter recruiter) {
        this.recruiter = recruiter;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}