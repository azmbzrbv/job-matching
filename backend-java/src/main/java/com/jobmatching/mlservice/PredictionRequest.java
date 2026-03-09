package com.jobmatching.mlservice;

import com.jobmatching.Job.Job;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record PredictionRequest(
        String resume_text,
        Map<Long, String> jobs // Map of JobID to JobDescription
) {}