package com.sc.salary.process.controller;


import com.sc.salary.process.Entity.EmployeeSalaryDetails;
import com.sc.salary.process.service.EmployeeSalaryDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employee-salaries")
public class EmployeeSalaryDetailsController {

    @Autowired
    private EmployeeSalaryDetailsService service;

    @GetMapping
    public List<EmployeeSalaryDetails> getEmployeeSalaries() {
        return service.getAllEmployeeSalaries();
    }
}