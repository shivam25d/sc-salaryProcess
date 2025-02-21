package com.sc.salary.process.service;

import com.sc.salary.process.Entity.EmployeeSalaryDetails;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocxGenerator {

    private static final String TEMPLATE_PATH = "salary_slip_template.docx"; // Inside resources/


    public File generateSalarySlip(EmployeeSalaryDetails employee) throws IOException {
        // Load the template from resources folder
        ClassPathResource resource = new ClassPathResource(TEMPLATE_PATH);
        File tempFile = File.createTempFile("temp_salary_slip", ".docx");

        try (InputStream is = resource.getInputStream();
             FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
        }

        // Open the copied template
        FileInputStream fis = new FileInputStream(tempFile);
        XWPFDocument document = new XWPFDocument(fis);
        fis.close();

        // Define the placeholders and their values
        Map<String, String> placeholders = getReplacementData(employee);

        // Replace placeholders in tables (DO NOT remove labels)
        replaceTablePlaceholders(document, placeholders);

        // Save the updated document
        File outputFile = new File("C:/salary_slips/" + employee.getEmpId() + "_SalarySlip.docx");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            document.write(fos);
        }
        document.close();

        return outputFile;
    }



    private Map<String, String> getReplacementData(EmployeeSalaryDetails details) {
        Map<String, String> data = new HashMap<>();

        // Employee Details
        data.put("Employee ID:", details.getEmpId());
        data.put("Designation:", details.getDesignation());
        data.put("Employee Name:", details.getEmployeeName());
        data.put("Client:", details.getClient());
        data.put("Date of Joining:", details.getDateOfJoining());
        data.put("Location:", details.getLocation());

        // Bank & Attendance
        data.put("Bank Name:", details.getBankName());
        data.put("Account Number:", details.getAccountNumber());
        data.put("Provident Fund No.:", details.getProvidentFundNo());
        data.put("PAN Card No.:", details.getPanCardNo());
        data.put("UAN No.:", details.getUanNo());
        data.put("ESIC No.:", details.getEsicNo());
        data.put("Working Days:", String.valueOf(details.getWorkingDays()));
        data.put("Attended Days:", String.valueOf(details.getAttendedDays()));
        data.put("Leave Availed:", String.valueOf(details.getLeaveAvailed()));
        data.put("Leave Balance:", String.valueOf(details.getLeaveBalance()));

        // Salary Slip Header
        data.put("SALARY SLIP FOR THE MONTH OF", details.getSalaryMonth());

        // Earnings Section - Fill Only Amounts
        data.put("Basic", String.valueOf(details.getBasic()));
        data.put("HRA", String.valueOf(details.getHra()));
        data.put("Conveyance", String.valueOf(details.getConveyance()));
        data.put("Medical Allowance", String.valueOf(details.getMedicalAllowance()));
        data.put("Telephone Allowance", String.valueOf(details.getTelephoneAllowance()));
        data.put("Bonus", String.valueOf(details.getBonus()));
        data.put("Canteen Allowance", String.valueOf(details.getCanteenAllowance()));
        data.put("Special Allowance", String.valueOf(details.getSpecialAllowance()));
        data.put("Arrears", String.valueOf(details.getArrears()));
        data.put("Reimbursement", String.valueOf(details.getReimbursement()));
        data.put("Total Earnings", String.valueOf(details.getTotalEarnings()));

        // Deductions Section - Fill Only Amounts
        data.put("Provident Fund", String.valueOf(details.getProvidentFund()));
        data.put("Professional Tax", String.valueOf(details.getProfessionalTax()));
        data.put("Income Tax (TDS)", String.valueOf(details.getIncomeTax()));
        data.put("Salary Advance", String.valueOf(details.getSalaryAdvance()));
        data.put("MLWF", String.valueOf(details.getMlwf()));
        data.put("ESIC", String.valueOf(details.getEsic()));
        data.put("Total Deductions", String.valueOf(details.getTotalDeductions()));

        // Net Salary
        data.put("Net Salary", String.valueOf(details.getNetSalary()));
        data.put("INR", "FORTY FOUR THOUSAND ONLY"); // Convert to words if needed

        return data;
    }





    private String safeValue(String value) {
        return (value == null) ? "" : value;
    }

    private void replaceTablePlaceholders(XWPFDocument document, Map<String, String> data) {
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                List<XWPFTableCell> cells = row.getTableCells();
                if (cells.size() >= 2) {  // Ensure row has at least two columns
                    String label = cells.get(0).getText().trim(); // First column (e.g., "Employee ID:")

                    if (data.containsKey(label)) {
                        String value = data.get(label); // Get the corresponding value

                        XWPFTableCell valueCell = cells.get(1); // Get the empty column
                        String existingText = valueCell.getText().trim();

                        if (existingText.isEmpty()) {  // Only replace if the field is blank
                            for (XWPFParagraph paragraph : valueCell.getParagraphs()) {
                                for (int i = paragraph.getRuns().size() - 1; i >= 0; i--) {
                                    paragraph.removeRun(i);
                                }
                                XWPFRun newRun = paragraph.createRun();
                                newRun.setText(value);
                                newRun.setFontSize(12); // Keep formatting
                            }
                        }
                    }
                }
            }
        }
    }



    private void replaceText(XWPFParagraph paragraph, Map<String, String> data) {
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs == null || runs.isEmpty()) {
            return;
        }

        String paragraphText = paragraph.getText().trim();

        // Check if paragraph contains a known label
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String label = entry.getKey();    // Label (e.g., "Employee ID:")
            String value = entry.getValue();  // Value (e.g., "EMP001")

            if (paragraphText.startsWith(label)) {
                String updatedText = label + " " + value; // Insert value after label

                // Remove old runs and insert new text
                for (int i = runs.size() - 1; i >= 0; i--) {
                    paragraph.removeRun(i);
                }
                XWPFRun newRun = paragraph.createRun();
                newRun.setText(updatedText);
                newRun.setFontSize(12); // Keep formatting consistent
                break;
            }
        }
    }

}