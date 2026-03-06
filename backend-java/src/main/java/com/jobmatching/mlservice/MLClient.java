package com.jobmatching.mlservice;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

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


}
