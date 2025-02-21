package com.sc.salary.process.controller;

import com.sc.salary.process.Entity.SCFileUpload;
import com.sc.salary.process.service.FileUploadService;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;


@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job processSalaryJob;

    // Maximum allowed file size (e.g., 5MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        // Validate file
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uploaded file is empty.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File size exceeds the maximum allowed size of 5MB.");
        }

        try {
            // Get the current user ID (replace with real logic in production)
            Long uploadedBy = getCurrentUserId();
            SCFileUpload uploadedFile = fileUploadService.uploadFile(file, uploadedBy);

            // Check if file metadata was saved successfully
            if (uploadedFile == null || uploadedFile.getFileUploadId() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("File upload failed: Unable to save file metadata.");
            }

            // Prepare job parameters
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("fileUploadId", uploadedFile.getFileUploadId())
                    .addLong("startAt", System.currentTimeMillis()) // Ensure uniqueness
                    .addString("fileName", file.getOriginalFilename()) // Add file name to parameters
                    .toJobParameters();

            // Launch the batch job for processing the uploaded file
            jobLauncher.run(processSalaryJob, jobParameters);

            return ResponseEntity.ok("File uploaded and processing started successfully.");
        } catch (Exception e) {
            logger.error("File upload failed: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }

    private Long getCurrentUserId() {
        return 1L; // Placeholder user ID, replace with actual user authentication logic
    }
}
