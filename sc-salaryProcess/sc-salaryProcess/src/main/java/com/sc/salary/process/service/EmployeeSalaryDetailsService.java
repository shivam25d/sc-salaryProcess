package com.sc.salary.process.service;

import com.sc.salary.process.Entity.EmployeeSalaryDetails;
import com.sc.salary.process.repository.EmployeeSalaryDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeSalaryDetailsService {

    @Autowired
    private EmployeeSalaryDetailsRepository repository;

    public List<EmployeeSalaryDetails> getAllEmployeeSalaries() {
        return repository.findAll();
    }
}