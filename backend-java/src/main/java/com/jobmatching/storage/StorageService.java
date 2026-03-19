package com.jobmatching.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file);
    Resource download(String filename); // For downloading later
    void delete(String filename);
}
