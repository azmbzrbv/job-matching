package com.jobmatching.storage;

import com.jobmatching.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Primary
public class FileSystemStorageService implements StorageService{

    private final Path rootLocation;

    public FileSystemStorageService(@Value("${file.upload-dir}") String uploadPath) {
        this.rootLocation = Paths.get(uploadPath);
    }

    //for uploading
    @Override
    public String store(MultipartFile file) {
        if (!isSupportedContentType(file.getContentType())) {
            throw new RuntimeException("Unsupported file type");
        }

        if (file.getSize() > 5_000_000) { // 5 MB size limit
            throw new RuntimeException("File size exceeds limits");
        }

        try {
            String cleanName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
            String finalFileName = UUID.randomUUID().toString() + "_" + cleanName;
            Files.copy(
                    file.getInputStream(),
                    this.rootLocation.resolve(finalFileName),
                    StandardCopyOption.REPLACE_EXISTING
            );
            return finalFileName;
        } catch (Exception e) {
            throw  new RuntimeException("File upload failed");
        }
    }

    //for downloading
    @Override
    public Resource download(String filename) {
        try {
            Path filePath = this.rootLocation.resolve(filename).normalize();

            if (!filePath.toAbsolutePath().startsWith(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Security: Access denied to path outside uploads.");
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("Could not read file: " + filename);
            }
            return resource;
        } catch (Exception e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            //Resolve the path to the specific file
            Path file = rootLocation.resolve(filename).normalize();

            //Ensure the file is inside our uploads directory
            if (!file.toAbsolutePath().startsWith(rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Security: Cannot delete files outside uploads directory");
            }
            //Delete the file if it exists
            boolean deleted = Files.deleteIfExists(file);
            if (!deleted) {
                System.out.println("Warning: Attempted to delete " + filename + " but it did not exist.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + filename, e);
        }
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("application/pdf");
    }
}
