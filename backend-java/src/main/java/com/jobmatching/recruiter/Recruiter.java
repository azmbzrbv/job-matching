package com.jobmatching.recruiter;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jobmatching.job.Job;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Recruiter name is required")
    private String name;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @JsonIgnore
    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL)
    private List<Job> jobs = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}