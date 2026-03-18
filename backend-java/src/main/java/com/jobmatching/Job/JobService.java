package com.jobmatching.Job;

import com.jobmatching.Job.dto.JobRequestDto;
import com.jobmatching.Job.dto.JobResponseDTO;
import com.jobmatching.Recruiter.Recruiter;
import com.jobmatching.Recruiter.RecruiterService;
import com.jobmatching.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class JobService {
    private final JobRepository jobRepository;
    private final RecruiterService recruiterService;

    public JobService(JobRepository jobRepository, RecruiterService recruiterService) {
        this.jobRepository = jobRepository;
        this.recruiterService = recruiterService;
    }

    //get prefix for the entities(other services will use it)
    //fetch prefix for the dto files(controller will use it)


    //methods for controller
    public List<JobResponseDTO> fetchAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        List<JobResponseDTO> jobResponseDTOS= new ArrayList<>();
        for(Job job: jobs){
            jobResponseDTOS.add(new JobResponseDTO(job, 0.0));
        }
        return jobResponseDTOS;
    }

    public JobResponseDTO fetchJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        return new JobResponseDTO(job, 0.0);
    }


    public JobResponseDTO createJob(JobRequestDto jobRequestDto) {
        Job newJob = new Job();
        newJob.setTitle(jobRequestDto.title());
        newJob.setDescription(jobRequestDto.description());
        Recruiter recruiter = recruiterService.getRecruiterById(jobRequestDto.recruiter_id());
        newJob.setRecruiter(recruiter);
        Job job = jobRepository.save(newJob);
        return new JobResponseDTO(job, 0.0);
    }

    public List<JobResponseDTO> fetchJobsByRecruiter(Long recruiterId) {
        List<Job> jobs = jobRepository.findByRecruiterId(recruiterId);
        return jobs.stream()
                .map(job->new JobResponseDTO(job, 0.0))
                .toList();
    }

    public List<JobResponseDTO> fetchJobsByTitle(String title) {
        List<Job> jobs;

        if (title == null || title.trim().isEmpty()) {
            jobs = jobRepository.findAll();
        } else {
            jobs = jobRepository.findByTitleContainingIgnoreCase(title);
        }
        return jobs.stream()
                .map(job -> new JobResponseDTO(job, 0.0))
                .toList();
    }

    //methods for other service classes
    public List<Job> getAllJobs(){
        return jobRepository.findAll();
    }
}
