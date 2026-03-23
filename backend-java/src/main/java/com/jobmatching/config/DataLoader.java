package com.jobmatching.config;

import com.jobmatching.candidate.Candidate;
import com.jobmatching.candidate.CandidateRepository;
import com.jobmatching.job.Job;
import com.jobmatching.recruiter.Recruiter;
import com.jobmatching.job.JobRepository;
import com.jobmatching.recruiter.RecruiterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
//@Profile("dev")  //running it in development mode
public class DataLoader implements CommandLineRunner {

    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;
    private final CandidateRepository candidateRepository;

    public DataLoader(JobRepository jobRepository, RecruiterRepository recruiterRepository, CandidateRepository candidateRepository) {
        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (recruiterRepository.count() == 0) {
            System.out.println("Starting database seeding...");
            seedDatabase();
            System.out.println("Seeding complete.");
        } else {
            System.out.println("Database already contains data. Skipping seeder.");
        }
    }

    private void seedDatabase() {
        // Create a Recruiter
        Recruiter techCorp = new Recruiter();
        techCorp.setName("Alice Johnson");
        techCorp.setCompanyName("TechCorp Solutions");
        techCorp.setEmail("hr@techcorp.com");
        recruiterRepository.save(techCorp);

        Candidate candidate = new Candidate();
        candidate.setFullName("Azim Maksatbek uulu");
        candidate.setEmail("bzrbvazm@gmail.com");
        candidateRepository.save(candidate);

        // Create sample Jobs linked to that Recruiter
        Job job1 = new Job();
        job1.setTitle("Full Stack Developer");
        job1.setDescription("Seeking a developer proficient in Spring Boot and React.");
        job1.setRecruiter(techCorp);

        Job job2 = new Job();
        job2.setTitle("Data Engineer");
        job2.setDescription("Looking for expertise in Python, SQL, and Vector Databases.");
        job2.setRecruiter(techCorp);

        jobRepository.saveAll(List.of(job1, job2));
    }
}