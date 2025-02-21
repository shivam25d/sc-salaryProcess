package com.sc.salary.process.service;

import com.sc.salary.process.Entity.BatchJobEntities.BatchJobInstance;
import com.sc.salary.process.repository.BatchJobInstanceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BatchJobService {

    private final BatchJobInstanceRepository jobInstanceRepository;

    public BatchJobService(BatchJobInstanceRepository jobInstanceRepository) {
        this.jobInstanceRepository = jobInstanceRepository;
    }

    public BatchJobInstance createJobInstance(BatchJobInstance jobInstance) {
        return jobInstanceRepository.save(jobInstance);
    }

    public Optional<BatchJobInstance> getJobInstance(Long id) {
        return jobInstanceRepository.findById(id);
    }

    // Other business methods
}
