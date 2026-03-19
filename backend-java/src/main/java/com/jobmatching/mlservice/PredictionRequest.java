package com.jobmatching.mlservice;

import java.util.Map;

public record PredictionRequest(
        String text,
        Map<Long, String> items // Map of JobID to JobDescription
) {}