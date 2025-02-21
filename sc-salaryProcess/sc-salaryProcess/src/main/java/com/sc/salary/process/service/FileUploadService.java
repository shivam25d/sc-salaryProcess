package com.sc.salary.process.service;

import com.sc.salary.process.Entity.EmployeeSalaryDetails;
import com.sc.salary.process.Entity.SCFileUpload;
import com.sc.salary.process.exception.handler.FileProcessingException;
import com.sc.salary.process.repository.EmployeeSalaryDetailsRepository;
import com.sc.salary.process.repository.SCFileUploadRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    @Autowired
    private SCFileUploadRepository fileUploadRepository;

    @Autowired
    private EmployeeSalaryDetailsRepository employeeSalaryDetailsRepository;

    private final String uploadDir = "C:/uploads/";

    public SCFileUpload uploadFile(MultipartFile file, Long uploadedBy) throws IOException {
        String serverFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = uploadDir + serverFileName;

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File destinationFile = new File(filePath);
        file.transferTo(destinationFile);

        SCFileUpload scFileUpload = new SCFileUpload();
        scFileUpload.setUploadedBy(uploadedBy);
        scFileUpload.setFileType("CSV");
        scFileUpload.setSourceFileName(file.getOriginalFilename());
        scFileUpload.setServerFileName(serverFileName);
        scFileUpload.setFileUploadDatetime(new Date());
        scFileUpload.setUploadStatus("LP");
        scFileUpload.setBatchCode("SALARY");
        fileUploadRepository.save(scFileUpload);

        processUploadedFile(destinationFile);

        return scFileUpload;
    }

    private void processUploadedFile(File file) {
        List<EmployeeSalaryDetails> employeeSalaryDetailsList = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            String[] header = csvReader.readNext();
            logger.info("Processing CSV file with headers: {}", Arrays.toString(header));

            String[] csvRecord;
            while ((csvRecord = csvReader.readNext()) != null) {
                try {
                    EmployeeSalaryDetails employee = createEmployeeFromCsv(csvRecord);
                    employee.setProcessStatus("PENDING");  // Status set to PENDING initially
                    employeeSalaryDetailsList.add(employee);
                } catch (FileProcessingException e) {
                    logger.warn("Skipping invalid record: {} due to error: {}", Arrays.toString(csvRecord), e.getMessage());
                }
            }

            // Log the number of records to be saved
            logger.info("Number of records to be saved: {}", employeeSalaryDetailsList.size());

            // Save all employee records into the database with 'PENDING' status
            employeeSalaryDetailsRepository.saveAll(employeeSalaryDetailsList);
            logger.info("Successfully saved employee salary details from file: {}", file.getName());
        } catch (IOException | CsvValidationException e) {
            logger.error("Error processing file: {}", file.getName(), e);
            throw new FileProcessingException("Error processing file: " + file.getName(), e);
        }
    }



    private EmployeeSalaryDetails createEmployeeFromCsv(String[] csvRecord) {
        // Update the column count check to match the new field count
        if (csvRecord.length < 41) {
            logger.warn("Skipping record with insufficient columns: {}", Arrays.toString(csvRecord));
            throw new FileProcessingException("CSV record has insufficient columns.");
        }

        EmployeeSalaryDetails employee = new EmployeeSalaryDetails();

        try {
            employee.setEmpId(csvRecord[1].trim());
            employee.setSalaryMonth(csvRecord[2].trim());
            employee.setMonthlySalary(parseDouble(csvRecord[3], 0.0));
            employee.setWorkingDays(parseInt(csvRecord[4], 0));
            employee.setAttendedDays(parseInt(csvRecord[5], 0));
            employee.setCalculatedAmount(parseDouble(csvRecord[6], 0.0));
            employee.setBasic(parseDouble(csvRecord[7], 0.0));
            employee.setHra(parseDouble(csvRecord[8], 0.0));
            employee.setBonus(parseDouble(csvRecord[9], 0.0));
            employee.setConveyance(parseDouble(csvRecord[10], 0.0));
            employee.setMedicalAllowance(parseDouble(csvRecord[11], 0.0));
            employee.setTelephoneAllowance(parseDouble(csvRecord[12], 0.0));
            employee.setSpecialAllowance(parseDouble(csvRecord[13], 0.0));
            employee.setCanteenAllowance(parseDouble(csvRecord[14], 0.0));
            employee.setArrears(parseDouble(csvRecord[15], 0.0));
            employee.setReimbursement(parseDouble(csvRecord[16], 0.0));
            employee.setTotalEarnings(parseDouble(csvRecord[17], 0.0));
            employee.setProvidentFund(parseDouble(csvRecord[18], 0.0));
            employee.setProfessionalTax(parseDouble(csvRecord[19], 0.0));
            employee.setIncomeTax(parseDouble(csvRecord[20], 0.0));
            employee.setSalaryAdvance(parseDouble(csvRecord[21], 0.0));
            employee.setMlwf(parseDouble(csvRecord[22], 0.0));
            employee.setEsic(parseDouble(csvRecord[23], 0.0));
            employee.setTotalDeductions(parseDouble(csvRecord[24], 0.0));
            employee.setNetSalary(parseDouble(csvRecord[25], 0.0));
            employee.setRemarks(csvRecord[26].trim());
            employee.setEmail(csvRecord[27].trim());
            employee.setLeaveAvailed(parseInt(csvRecord[28], 0));
            employee.setLeaveBalance(parseInt(csvRecord[29], 0));

            // Additional Fields from the template
            employee.setDesignation(csvRecord[30].trim());
            employee.setEmployeeName(csvRecord[31].trim());
            employee.setClient(csvRecord[32].trim());
            employee.setDateOfJoining(csvRecord[33].trim());
            employee.setLocation(csvRecord[34].trim());
            employee.setBankName(csvRecord[35].trim());
            employee.setAccountNumber(csvRecord[36].trim());
            employee.setProvidentFundNo(csvRecord[37].trim());
            employee.setPanCardNo(csvRecord[38].trim());
            employee.setUanNo(csvRecord[39].trim());
            employee.setEsicNo(csvRecord[40].trim());

        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error("CSV record has missing fields: {}", Arrays.toString(csvRecord), e);
            throw new FileProcessingException("CSV record has missing fields", e);
        } catch (NumberFormatException e) {
            logger.error("Invalid number format in CSV record: {}", Arrays.toString(csvRecord), e);
            throw new FileProcessingException("Invalid number format in CSV record", e);
        }

        return employee;
    }


    private double parseDouble(String value, double defaultValue) {
        try {
            return (value == null || value.isEmpty()) ? defaultValue : Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Invalid double format for value: '{}', defaulting to {}", value, defaultValue);
            return defaultValue;
        }
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return (value == null || value.isEmpty()) ? defaultValue : Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer format for value: '{}', defaulting to {}", value, defaultValue);
            return defaultValue;
        }
    }
}