package com.jobmatching.exception;

public record ErrorResponse(
        int status,
        String message,
        long timestamp
) {}