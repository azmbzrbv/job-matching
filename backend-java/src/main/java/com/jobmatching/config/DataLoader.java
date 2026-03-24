package com.jobmatching.config;

import com.jobmatching.candidate.Candidate;
import com.jobmatching.candidate.CandidateRepository;
import com.jobmatching.job.Job;
import com.jobmatching.recruiter.Recruiter;
import com.jobmatching.job.JobRepository;
import com.jobmatching.recruiter.RecruiterRepository;
import com.jobmatching.user.Role;
import com.jobmatching.user.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
//@Profile("dev")  //running it in development mode
public class DataLoader implements CommandLineRunner {

    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;
    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(JobRepository jobRepository, RecruiterRepository recruiterRepository, CandidateRepository candidateRepository, PasswordEncoder passwordEncoder) {
        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
        this.candidateRepository = candidateRepository;
        this.passwordEncoder = passwordEncoder;
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
        //Create Recruiter User Account
        User recruiterUser = new User();
        recruiterUser.setEmail("hr@techcorp.com");
        recruiterUser.setPassword(passwordEncoder.encode("password123")); // HASHED!
        recruiterUser.setRole(Role.ROLE_RECRUITER);

        //Create Recruiter Profile and Link to User
        Recruiter techCorp = new Recruiter();
        techCorp.setName("Alice Smith");
        techCorp.setCompanyName("TechCorp Solutions");
        techCorp.setUser(recruiterUser);
        recruiterRepository.save(techCorp); // Cascade.ALL saves the user automatically

        //Create Candidate User Account
        User candidateUser = new User();
        candidateUser.setEmail("bzrbvazm@gmail.com");
        candidateUser.setPassword(passwordEncoder.encode("candidate123"));
        candidateUser.setRole(Role.ROLE_CANDIDATE);

        // Create Candidate Profile and Link
        Candidate candidate = new Candidate();
        candidate.setFullName("Azim Maksatbek uulu");
        candidate.setUser(candidateUser);
        candidateRepository.save(candidate); //saves user as well

        // create Jobs (Same as before)
        Job job1 = new Job();
        job1.setTitle("Full Stack Developer");
        job1.setDescription("Seeking a developer proficient in Spring Boot and React.");
        job1.setRecruiter(techCorp);

        jobRepository.save(job1);
    }
}