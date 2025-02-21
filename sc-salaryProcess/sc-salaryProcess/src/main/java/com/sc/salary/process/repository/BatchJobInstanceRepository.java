package com.sc.salary.process.repository;

import com.sc.salary.process.Entity.BatchJobEntities.BatchJobInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchJobInstanceRepository extends JpaRepository<BatchJobInstance, Long> {

}
