package com.sc.salary.process.config;

import com.sc.salary.process.Entity.EmployeeSalaryDetails;
import com.sc.salary.process.repository.EmployeeSalaryDetailsRepository;
import com.sc.salary.process.service.EmailService;
import com.sc.salary.process.service.DocxGenerator;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Value("${file.docx.dir}")
    private String docxDir;


    @Autowired
    private EmployeeSalaryDetailsRepository employeeSalaryDetailsRepository;

    @Autowired
    private DocxGenerator docxGenerator;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;



    @Bean
    public Job processSalaryJob(JobExecutionListener listener) {
        return new JobBuilder("processSalaryJob", jobRepository)
                .start(loadEmployeeSalaryDetailsStep())
                .next(processEmployeeSalaryDetailsStep())
                .listener(listener)
                .build();
    }

    @Bean
    public Step loadEmployeeSalaryDetailsStep() {
        return new StepBuilder("loadEmployeeSalaryDetailsStep", jobRepository)
                .<EmployeeSalaryDetails, EmployeeSalaryDetails>chunk(10, transactionManager)
                .reader(employeeSalaryDetailsCsvReader(null))
                .processor(employeeSalaryDetailsProcessor())
                .writer(employeeSalaryDetailsWriter())
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(10)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        System.out.println("Opening the CSV reader.");
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        System.out.println("Reader Status: " + stepExecution.getReadCount());
                        System.out.println("Skipped Count: " + stepExecution.getSkipCount());
                        System.out.println("Write Count: " + stepExecution.getWriteCount());

                        if (stepExecution.getSkipCount() > 0) {
                            System.out.println("Some rows were skipped due to errors.");
                        }

                        return stepExecution.getExitStatus();
                    }
                })
                .build();
    }

    @Bean
    public Step processEmployeeSalaryDetailsStep() {
        return new StepBuilder("processEmployeeSalaryDetailsStep", jobRepository)
                .<EmployeeSalaryDetails, EmployeeSalaryDetails>chunk(10, transactionManager)
                .reader(employeeSalaryDetailsJpaReader())
                .processor(employeeSalaryDetailsProcessor())
                .writer(employeeSalaryDetailsWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<EmployeeSalaryDetails> employeeSalaryDetailsCsvReader(
            @Value("#{jobParameters['filePath']}") String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            File latestFile = getLatestFile(uploadDir);
            filePath = latestFile.getAbsolutePath();
            System.out.println("Using latest file: " + filePath);
        }

        return new FlatFileItemReaderBuilder<EmployeeSalaryDetails>()
                .name("employeeSalaryDetailsCsvReader")
                .resource(new FileSystemResource(filePath))
                .delimited()
                .names("empId", "salaryMonth", "monthlySalary", "workingDays", "attendedDays", "calculatedAmount",
                        "basic", "hra", "bonus", "conveyance", "medicalAllowance", "telephoneAllowance", "specialAllowance",
                        "canteenAllowance", "arrears", "reimbursement", "totalEarnings", "providentFund", "professionalTax",
                        "incomeTax", "salaryAdvance", "mlwf", "esic", "totalDeductions", "netSalary", "remarks", "email",
                        "leaveAvailed", "leaveBalance", "designation", "employeeName", "client", "dateOfJoining",
                        "location", "bankName", "accountNumber", "providentFundNo", "panCardNo", "uanNo", "esicNo")
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(EmployeeSalaryDetails.class);
                }})
                .build();
    }

    @Bean
    public JpaPagingItemReader<EmployeeSalaryDetails> employeeSalaryDetailsJpaReader() {
        JpaPagingItemReader<EmployeeSalaryDetails> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT e FROM EmployeeSalaryDetails e");
        reader.setPageSize(10);
        return reader;
    }

    @Bean
    public ItemProcessor<EmployeeSalaryDetails, EmployeeSalaryDetails> employeeSalaryDetailsProcessor() {
        return item -> {
            StringBuilder errorMsg = new StringBuilder();

            // Validate required fields
            if (item.getEmpId() == null || item.getEmpId().isEmpty()) errorMsg.append("Missing empId. ");
            if (item.getSalaryMonth() == null || item.getSalaryMonth().isEmpty()) errorMsg.append("Missing salaryMonth. ");
            if (item.getMonthlySalary() == null || item.getMonthlySalary() <= 0) errorMsg.append("Invalid monthlySalary. ");
            if (item.getWorkingDays() == null || item.getWorkingDays() <= 0) errorMsg.append("Invalid workingDays. ");
            if (item.getAttendedDays() == null || item.getAttendedDays() < 0) errorMsg.append("Invalid attendedDays. ");
            if (item.getTotalEarnings() == null || item.getTotalEarnings() <= 0) errorMsg.append("Invalid totalEarnings. ");
            if (item.getTotalDeductions() == null || item.getTotalDeductions() < 0) errorMsg.append("Invalid totalDeductions. ");
            if (item.getNetSalary() == null || item.getNetSalary() <= 0) errorMsg.append("Invalid netSalary. ");
            if (item.getEmail() == null || item.getEmail().isEmpty()) errorMsg.append("Missing email. ");
            if (item.getLeaveAvailed() == null || item.getLeaveAvailed() < 0) errorMsg.append("Invalid leaveAvailed. ");
            if (item.getLeaveBalance() == null || item.getLeaveBalance() < 0) errorMsg.append("Invalid leaveBalance. ");
            if (item.getRemarks() == null || item.getRemarks().isEmpty()) errorMsg.append("Missing remarks. ");


            if (errorMsg.length() > 0) {
                item.setProcessStatus("Pending");
                item.setProcessComment("Record failed due to: " + errorMsg.toString());
                return item;
            }

            item.setProcessStatus("PROCESSED");
            item.setProcessComment("Successfully processed the salary details.");
            return item;
        };
    }

    @Bean
    public ItemWriter<EmployeeSalaryDetails> employeeSalaryDetailsWriter() {
        return items -> {
            for (EmployeeSalaryDetails item : items) {
                try {
                    // Save the employee salary details
                    employeeSalaryDetailsRepository.save(item);
                    System.out.println("Saved EmployeeSalaryDetails for Employee ID: " + item.getEmpId() + ", Status: " + item.getProcessStatus());

                    // Process only if the status is PROCESSED
                    if ("PROCESSED".equalsIgnoreCase(item.getProcessStatus())) {
                        try {
                            // Generate the salary slip
                            File docxFile = docxGenerator.generateSalarySlip(item);

                            // Send email with attachment
                            emailService.sendEmailWithAttachment(
                                    item.getEmail(),
                                    "Your Monthly Salary Slip",
                                    "Please find attached your salary slip for this month.",
                                    docxFile.getAbsolutePath()
                            );

                            System.out.println("Successfully sent email with PDF for Employee ID: " + item.getEmpId());
                        } catch (Exception e) {
                            System.err.println("Error during email sending or PDF generation for Employee ID: " + item.getEmpId());
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Skipping email and PDF generation for Employee ID: " + item.getEmpId() + " due to processing failure.");
                    }
                } catch (Exception e) {
                    System.err.println("Failed to save EmployeeSalaryDetails for Employee ID: " + item.getEmpId());
                    e.printStackTrace();
                }
            }
        };
    }


    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListenerSupport() {
            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.FAILED) {
                    jobExecution.getAllFailureExceptions().forEach(e -> {
                        System.err.println("Job failed due to: " + e.getMessage());
                        e.printStackTrace();
                    });
                } else {
                    System.out.println("Job completed successfully.");
                }
            }
        };
    }

    private File getLatestFile(String uploadDir) {
        File dir = new File(uploadDir);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".csv"));

        if (files == null || files.length == 0) {
            throw new IllegalStateException("No CSV files found in the directory: " + uploadDir);
        }

        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        return files[0];
    }
}