package com.jobmatching.mlservice;


import com.jobmatching.Candidate.Candidate;
import com.jobmatching.Job.Job;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MLClient {

    private final WebClient webClient;

    //inject the weblient that we created in configuration class
    public MLClient(WebClient webClient){
        this.webClient = webClient;
    }


    public String extractTextFromPDF(MultipartFile file) {
        return webClient.post()
                .uri("/extract-text") // The endpoint we will create in Python
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", file.getResource()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .map(response -> response.get("extracted_text"))
                .block(); // Waits for the response (Synchronous behavior)
    }



    public Map<Long, Double> rankJobs(String resumeText, List<Job> jobs) {
        Map<Long, String> jobMap = jobs.stream()
                .collect(Collectors.toMap(Job::getId, Job::getDescription));

        PredictionRequest request = new PredictionRequest(resumeText, jobMap);

        // 2. Send to Python FastAPI
        return webClient.post()
                .uri("/rank-jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                // We expect a Map where Key is JobID and Value is the Match Score
                .bodyToMono(new ParameterizedTypeReference<Map<Long, Double>>() {})
                .block();
    }


    //TODO: change them to single comparison after updating the python functionality
    public Map<Long, Double> rankCandidates(String jobDescription, List<Candidate> candidates){
        Map<Long, String> candidateMap = candidates.stream()
                .collect(Collectors.toMap(Candidate::getId, Candidate::getResumeText));

        PredictionRequest request = new PredictionRequest(jobDescription, candidateMap);
        return webClient.post()
                .uri("/rank-candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<Long, Double>>() {})
                .block();
    }


}
