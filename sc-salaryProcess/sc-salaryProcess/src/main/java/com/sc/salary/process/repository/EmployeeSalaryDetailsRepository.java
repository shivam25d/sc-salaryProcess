package com.sc.salary.process.repository;


import com.sc.salary.process.Entity.EmployeeSalaryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSalaryDetailsRepository extends JpaRepository<EmployeeSalaryDetails, Long  > {
}