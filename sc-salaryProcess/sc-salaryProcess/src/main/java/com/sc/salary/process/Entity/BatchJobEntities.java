package com.sc.salary.process.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

public class BatchJobEntities {

    @Entity
    @Table(name = "BATCH_JOB_INSTANCE", uniqueConstraints = {
            @UniqueConstraint(columnNames = {"JOB_NAME", "JOB_KEY"}, name = "JOB_INST_UN")
    })
    public static class BatchJobInstance {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "batch_job_seq")
        @SequenceGenerator(name = "batch_job_seq", sequenceName = "BATCH_JOB_SEQ", allocationSize = 1)
        @Column(name = "JOB_INSTANCE_ID")
        private Long jobInstanceId;

        @Column(name = "VERSION")
        private Long version;

        @Column(name = "JOB_NAME", nullable = false, length = 100)
        private String jobName;

        @Column(name = "JOB_KEY", nullable = false, length = 32)
        private String jobKey;

        @OneToMany(mappedBy = "jobInstance", cascade = CascadeType.ALL)
        private Set<BatchJobExecution> jobExecutions;

        // Getters and setters
    }

    @Entity
    @Table(name = "BATCH_JOB_EXECUTION")
    public static class BatchJobExecution {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "batch_job_execution_seq")
        @SequenceGenerator(name = "batch_job_execution_seq", sequenceName = "BATCH_JOB_EXECUTION_SEQ", allocationSize = 1)
        @Column(name = "JOB_EXECUTION_ID")
        private Long jobExecutionId;

        @Column(name = "VERSION")
        private Long version;

        @ManyToOne
        @JoinColumn(name = "JOB_INSTANCE_ID", nullable = false, foreignKey = @ForeignKey(name = "JOB_INST_EXEC_FK"))
        private BatchJobInstance jobInstance;

        @Column(name = "CREATE_TIME", nullable = false)
        private LocalDateTime createTime;

        @Column(name = "START_TIME")
        private LocalDateTime startTime;

        @Column(name = "END_TIME")
        private LocalDateTime endTime;

        @Column(name = "STATUS", length = 10)
        private String status;

        @Column(name = "EXIT_CODE", length = 2500)
        private String exitCode;

        @Column(name = "EXIT_MESSAGE", length = 2500)
        private String exitMessage;

        @Column(name = "LAST_UPDATED")
        private LocalDateTime lastUpdated;

        @OneToMany(mappedBy = "jobExecution", cascade = CascadeType.ALL)
        private Set<BatchStepExecution> stepExecutions;

        @OneToOne(mappedBy = "jobExecution", cascade = CascadeType.ALL)
        private BatchJobExecutionContext executionContext;

        // Getters and setters
    }

    @Entity
    @Table(name = "BATCH_JOB_EXECUTION_PARAMS")
    public static class BatchJobExecutionParams {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID")
        private Long id;

        @ManyToOne
        @JoinColumn(name = "JOB_EXECUTION_ID", nullable = false, foreignKey = @ForeignKey(name = "JOB_EXEC_PARAMS_FK"))
        private BatchJobExecution jobExecution;

        @Column(name = "PARAMETER_NAME", nullable = false, length = 100)
        private String parameterName;

        @Column(name = "PARAMETER_TYPE", nullable = false, length = 100)
        private String parameterType;

        @Column(name = "PARAMETER_VALUE", length = 2500)
        private String parameterValue;

        @Column(name = "IDENTIFYING", nullable = false, length = 1)
        private char identifying;

        // Getters and setters
    }

    @Entity
    @Table(name = "BATCH_STEP_EXECUTION")
    public static class BatchStepExecution {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "batch_step_execution_seq")
        @SequenceGenerator(name = "batch_step_execution_seq", sequenceName = "BATCH_STEP_EXECUTION_SEQ", allocationSize = 1)
        @Column(name = "STEP_EXECUTION_ID")
        private Long stepExecutionId;

        @Column(name = "VERSION", nullable = false)
        private Long version;

        @Column(name = "STEP_NAME", nullable = false, length = 100)
        private String stepName;

        @ManyToOne
        @JoinColumn(name = "JOB_EXECUTION_ID", nullable = false, foreignKey = @ForeignKey(name = "JOB_EXEC_STEP_FK"))
        private BatchJobExecution jobExecution;

        @Column(name = "CREATE_TIME", nullable = false)
        private LocalDateTime createTime;

        @Column(name = "START_TIME")
        private LocalDateTime startTime;

        @Column(name = "END_TIME")
        private LocalDateTime endTime;

        @Column(name = "STATUS", length = 10)
        private String status;

        @Column(name = "COMMIT_COUNT")
        private Long commitCount;

        @Column(name = "READ_COUNT")
        private Long readCount;

        @Column(name = "FILTER_COUNT")
        private Long filterCount;

        @Column(name = "WRITE_COUNT")
        private Long writeCount;

        @Column(name = "READ_SKIP_COUNT")
        private Long readSkipCount;

        @Column(name = "WRITE_SKIP_COUNT")
        private Long writeSkipCount;

        @Column(name = "PROCESS_SKIP_COUNT")
        private Long processSkipCount;

        @Column(name = "ROLLBACK_COUNT")
        private Long rollbackCount;

        @Column(name = "EXIT_CODE", length = 2500)
        private String exitCode;

        @Column(name = "EXIT_MESSAGE", length = 2500)
        private String exitMessage;

        @Column(name = "LAST_UPDATED")
        private LocalDateTime lastUpdated;

        @OneToOne(mappedBy = "stepExecution", cascade = CascadeType.ALL)
        private BatchStepExecutionContext executionContext;

        // Getters and setters
    }

    @Entity
    @Table(name = "BATCH_STEP_EXECUTION_CONTEXT")
    public static class BatchStepExecutionContext {

        @Id
        @Column(name = "STEP_EXECUTION_ID")
        private Long stepExecutionId;

        @OneToOne
        @MapsId
        @JoinColumn(name = "STEP_EXECUTION_ID", foreignKey = @ForeignKey(name = "STEP_EXEC_CTX_FK"))
        private BatchStepExecution stepExecution;

        @Column(name = "SHORT_CONTEXT", nullable = false, length = 2500)
        private String shortContext;

        @Column(name = "SERIALIZED_CONTEXT", columnDefinition = "LONGVARCHAR")
        private String serializedContext;

        // Getters and setters
    }

    @Entity
    @Table(name = "BATCH_JOB_EXECUTION_CONTEXT")
    public static class BatchJobExecutionContext {

        @Id
        @Column(name = "JOB_EXECUTION_ID")
        private Long jobExecutionId;

        @OneToOne
        @MapsId
        @JoinColumn(name = "JOB_EXECUTION_ID", foreignKey = @ForeignKey(name = "JOB_EXEC_CTX_FK"))
        private BatchJobExecution jobExecution;

        @Column(name = "SHORT_CONTEXT", nullable = false, length = 2500)
        private String shortContext;

        @Column(name = "SERIALIZED_CONTEXT", columnDefinition = "LONGVARCHAR")
        private String serializedContext;

        // Getters and setters
    }
}
