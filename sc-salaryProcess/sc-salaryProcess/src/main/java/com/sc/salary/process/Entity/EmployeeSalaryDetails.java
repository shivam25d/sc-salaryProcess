package com.sc.salary.process.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "employee_salary_details")
public @Data class EmployeeSalaryDetails {

    
    @Id
    @Column(name = "employee_id")
    private String empId;

    @Column(name = "salary_month")
    private String salaryMonth;

    @Column(name = "monthly_salary")
    private Double monthlySalary;

    @Column(name = "working_days")
    private Integer workingDays;

    @Column(name = "attended_days")
    private Integer attendedDays;

    @Column(name = "calculated_amount")
    private Double calculatedAmount;

    @Column(name = "basic")
    private Double basic;

    @Column(name = "hra")
    private Double hra;

    @Column(name = "bonus")
    private Double bonus;

    @Column(name = "conveyance")
    private Double conveyance;

    @Column(name = "medical_allowance")
    private Double medicalAllowance;

    @Column(name = "telephone_allowance")
    private Double telephoneAllowance;

    @Column(name = "special_allowance")
    private Double specialAllowance;

    @Column(name = "canteen_allowance")
    private Double canteenAllowance;

    @Column(name = "arrears")
    private Double arrears;

    @Column(name = "reimbursement")
    private Double reimbursement;

    @Column(name = "total_earnings")
    private Double totalEarnings;

    @Column(name = "provident_fund")
    private Double providentFund;

    @Column(name = "professional_tax")
    private Double professionalTax;

    @Column(name = "income_tax")
    private Double incomeTax;

    @Column(name = "salary_advance")
    private Double salaryAdvance;

    @Column(name = "mlwf")
    private Double mlwf;

    @Column(name = "esic")
    private Double esic;

    @Column(name = "total_deductions")
    private Double totalDeductions;

    @Column(name = "net_salary")
    private Double netSalary;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "email")
    private String email;

    @Column(name = "leave_availed")
    private Integer leaveAvailed;

    @Column(name = "leave_balance")
    private Integer leaveBalance;

    @Column(name = "designation")
    private String designation;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "client")
    private String client;

    @Column(name = "date_of_joining")
    private String dateOfJoining;

    @Column(name = "location")
    private String location;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "provident_fund_no")
    private String providentFundNo;

    @Column(name = "pan_card_no")
    private String panCardNo;

    @Column(name = "uan_no")
    private String uanNo;

    @Column(name = "esic_no")
    private String esicNo;

    @Column(name = "process_status")
    private String processStatus;

    @Column(name = "process_comment")
    private String processComment;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getSalaryMonth() {
        return salaryMonth;
    }

    public void setSalaryMonth(String salaryMonth) {
        this.salaryMonth = salaryMonth;
    }

    public Double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(Double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    public Integer getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Integer workingDays) {
        this.workingDays = workingDays;
    }

    public Integer getAttendedDays() {
        return attendedDays;
    }

    public void setAttendedDays(Integer attendedDays) {
        this.attendedDays = attendedDays;
    }

    public Double getCalculatedAmount() {
        return calculatedAmount;
    }

    public void setCalculatedAmount(Double calculatedAmount) {
        this.calculatedAmount = calculatedAmount;
    }

    public Double getBasic() {
        return basic;
    }

    public void setBasic(Double basic) {
        this.basic = basic;
    }

    public Double getHra() {
        return hra;
    }

    public void setHra(Double hra) {
        this.hra = hra;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public Double getConveyance() {
        return conveyance;
    }

    public void setConveyance(Double conveyance) {
        this.conveyance = conveyance;
    }

    public Double getMedicalAllowance() {
        return medicalAllowance;
    }

    public void setMedicalAllowance(Double medicalAllowance) {
        this.medicalAllowance = medicalAllowance;
    }

    public Double getTelephoneAllowance() {
        return telephoneAllowance;
    }

    public void setTelephoneAllowance(Double telephoneAllowance) {
        this.telephoneAllowance = telephoneAllowance;
    }

    public Double getSpecialAllowance() {
        return specialAllowance;
    }

    public void setSpecialAllowance(Double specialAllowance) {
        this.specialAllowance = specialAllowance;
    }

    public Double getCanteenAllowance() {
        return canteenAllowance;
    }

    public void setCanteenAllowance(Double canteenAllowance) {
        this.canteenAllowance = canteenAllowance;
    }

    public Double getArrears() {
        return arrears;
    }

    public void setArrears(Double arrears) {
        this.arrears = arrears;
    }

    public Double getReimbursement() {
        return reimbursement;
    }

    public void setReimbursement(Double reimbursement) {
        this.reimbursement = reimbursement;
    }

    public Double getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(Double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public Double getProvidentFund() {
        return providentFund;
    }

    public void setProvidentFund(Double providentFund) {
        this.providentFund = providentFund;
    }

    public Double getProfessionalTax() {
        return professionalTax;
    }

    public void setProfessionalTax(Double professionalTax) {
        this.professionalTax = professionalTax;
    }

    public Double getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(Double incomeTax) {
        this.incomeTax = incomeTax;
    }

    public Double getSalaryAdvance() {
        return salaryAdvance;
    }

    public void setSalaryAdvance(Double salaryAdvance) {
        this.salaryAdvance = salaryAdvance;
    }

    public Double getMlwf() {
        return mlwf;
    }

    public void setMlwf(Double mlwf) {
        this.mlwf = mlwf;
    }

    public Double getEsic() {
        return esic;
    }

    public void setEsic(Double esic) {
        this.esic = esic;
    }

    public Double getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(Double totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLeaveAvailed() {
        return leaveAvailed;
    }

    public void setLeaveAvailed(Integer leaveAvailed) {
        this.leaveAvailed = leaveAvailed;
    }

    public Integer getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(Integer leaveBalance) {
        this.leaveBalance = leaveBalance;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getProvidentFundNo() {
        return providentFundNo;
    }

    public void setProvidentFundNo(String providentFundNo) {
        this.providentFundNo = providentFundNo;
    }

    public String getPanCardNo() {
        return panCardNo;
    }

    public void setPanCardNo(String panCardNo) {
        this.panCardNo = panCardNo;
    }

    public String getUanNo() {
        return uanNo;
    }

    public void setUanNo(String uanNo) {
        this.uanNo = uanNo;
    }

    public String getEsicNo() {
        return esicNo;
    }

    public void setEsicNo(String esicNo) {
        this.esicNo = esicNo;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getProcessComment() {
        return processComment;
    }

    public void setProcessComment(String processComment) {
        this.processComment = processComment;
    }
}