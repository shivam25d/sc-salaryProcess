//package com.sc.salary.process.Scheduler;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SalaryProcessingScheduler {
//
//    @Autowired
//    private JobLauncher jobLauncher;
//
//    @Autowired
//    private Job processSalaryJob;
//
//    @Scheduled(cron = "0 0 1 1 * ?" )
//    public void scheduleSalaryProcessing() {
//        try {
//            JobParameters jobParameters = new JobParametersBuilder()
//                    .addLong("startAt", System.currentTimeMillis())
//                    .toJobParameters();
//            jobLauncher.run(processSalaryJob, jobParameters);
//        } catch (Exception e) {
//            e.printStackTrace(); // Log error and handle it appropriately
//        }
//    }
//}
