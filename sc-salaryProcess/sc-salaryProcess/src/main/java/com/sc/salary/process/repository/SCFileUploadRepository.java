package com.sc.salary.process.repository;

import com.sc.salary.process.Entity.SCFileUpload;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SCFileUploadRepository extends JpaRepository<SCFileUpload, Long> {
    SCFileUpload findTopByOrderByFileUploadDatetimeDesc();
}
